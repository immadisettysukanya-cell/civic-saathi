package com.civicsaathi.civic_saathi.chat;



import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.civicsaathi.civic_saathi.model.Scheme;
import com.civicsaathi.civic_saathi.service.SchemeService;



@Service

public class ChatService {



    private final RestTemplate restTemplate = new RestTemplate();

    private final SchemeService schemeService;



    private static final Set<String> GREETINGS = Set.of(

            "hi", "hello", "hey",

            "నమస్కారం", "నమస్తే", "హాయ్", "హలో"

    );



    @Value("${civicsaathi.ollama.url:http://localhost:11434}")

    private String ollamaUrl;



    @Value("${civicsaathi.ollama.model:llama3.2}")

    private String model;



    public ChatService(SchemeService schemeService) {

        this.schemeService = schemeService;

    }



    public String chat(

            String message,

            String language,

            List<ChatController.ChatMessage> history) {



        if (message == null || message.isBlank()) {

            throw new ResponseStatusException(

                    HttpStatus.BAD_REQUEST,

                    "Please enter a message."

            );

        }



        boolean telugu = isTelugu(language);

        System.out.println("[CivicSaathi] requestedLanguage=[" + language + "], selectedLanguage=" + (telugu ? "Telugu" : "English"));



        String normalizedMessage = message.trim()

                .replaceAll("[\\p{P}]+$", "")

                .trim()

                .toLowerCase(Locale.ROOT);



        // 1. Handle greetings without calling the AI.

                if (GREETINGS.contains(normalizedMessage)) {
            return telugu
                    ? "నమస్కారం! నేను CivicSaathi. ప్రభుత్వ పథకాలు మరియు ప్రజా సేవల గురించి తెలుసుకోవడంలో మీకు ఎలా సహాయం చేయగలను?"
                    : "Hello! I'm CivicSaathi. How can I help you explore government schemes and public services?";
        }



        // Answer questions about CivicSaathi consistently without calling the AI.
        boolean asksAboutRole =
                normalizedMessage.contains("your role")
                || normalizedMessage.contains("your purpose")
                || normalizedMessage.contains("what is civicsaathi")
                || normalizedMessage.contains("what does civicsaathi do")
                || normalizedMessage.contains("about civicsaathi")
                || normalizedMessage.contains("explain your role")
                || normalizedMessage.contains("who are you")
                || normalizedMessage.contains("పాత్ర")
                || normalizedMessage.contains("సివిక్ సాథీగా")
                || normalizedMessage.contains("సివిక్ సాథీ అంటే")
                || normalizedMessage.contains("మీరు ఎవరు")
                || normalizedMessage.contains("మీ ఉద్దేశ్యం");

        if (asksAboutRole) {
            return telugu
                    ? "నేను CivicSaathi, ప్రభుత్వ పథకాలు మరియు ప్రజా సేవల గురించి సులభంగా తెలుసుకోవడంలో మీకు సహాయపడే డిజిటల్ సహాయకుడిని. పథకాల అర్హతలు, అవసరమైన పత్రాలు మరియు అధికారిక వెబ్‌సైట్ల గురించి మార్గనిర్దేశం చేస్తాను."
                    : "I'm CivicSaathi, a digital assistant that helps you understand government schemes and public services in India. I can guide you to information about eligibility, required documents, and official websites.";
        }
        // 2. Load the current scheme catalogue.

        List<Scheme> schemes = schemeService.getAllSchemes();



        if (schemes == null) {

            schemes = List.of();

        }



        // Handle Telugu PM-KISAN application questions before farmer routing.
        if (telugu && isApplicationQuestion(message)) {
            String pmKisanQuery = message.toLowerCase(Locale.ROOT);

            if (pmKisanQuery.contains("pm-kisan")
                    || pmKisanQuery.contains("pm kisan")
                    || pmKisanQuery.contains("pradhan mantri kisan samman nidhi")) {
                return "PM-KISAN \u0c2a\u0c25\u0c15\u0c3e\u0c28\u0c3f\u0c15\u0c3f \u0c26\u0c30\u0c16\u0c3e\u0c38\u0c4d\u0c24\u0c41 \u0c1a\u0c47\u0c2f\u0c21\u0c3e\u0c28\u0c3f\u0c15\u0c3f, \u0c24\u0c3e\u0c1c\u0c3e \u0c35\u0c3f\u0c35\u0c30\u0c3e\u0c32\u0c41 \u0c2e\u0c30\u0c3f\u0c2f\u0c41 \u0c05\u0c35\u0c38\u0c30\u0c2e\u0c48\u0c28 \u0c2a\u0c24\u0c4d\u0c30\u0c3e\u0c32 \u0c15\u0c4b\u0c38\u0c02 \u0c05\u0c27\u0c3f\u0c15\u0c3e\u0c30\u0c3f\u0c15 \u0c35\u0c46\u0c2c\u0c4d\u200c\u0c38\u0c48\u0c1f\u0c4d \u0c1a\u0c42\u0c21\u0c02\u0c21\u0c3f: https://www.pmkisan.gov.in/";
            }
        }
        // 3. Handle farmer questions using catalogue data.

        if (isFarmerSchemeQuestion(message)) {

            return buildFarmerSchemeResponse(

                    findFarmerSchemes(schemes),

                    telugu

            );

        }



        // 4. Answer simple English education-list questions from the catalogue.

        if (!telugu && isEnglishEducationListQuestion(message)) {

            return buildEducationSchemeListResponse(schemes);

        }





        // 4. Handle common Telugu questions.

                if (telugu && isGeneralSchemeQuestion(message)) {
            return "తప్పకుండా! ప్రభుత్వ పథకాలు, వాటి అర్హతలు, అవసరమైన పత్రాలు, దరఖాస్తు విధానం గురించి తెలుసుకోవడంలో సహాయం చేస్తాను. "
                    + "మీ రాష్ట్రం, చదువు లేదా వృత్తి వివరాలు చెబితే, మీకు సరిపోయే పథకాలను గుర్తించడంలో సహాయం చేయగలను. "
                    + "తాజా వివరాల కోసం సంబంధిత అధికారిక వెబ్‌సైట్‌ను ధృవీకరించండి.";
        }



                if (telugu && isStudentSchemeQuestion(message)) {
            return "విద్యార్థుల కోసం అందుబాటులో ఉన్న పథకాల గురించి తెలుసుకోవడానికి Post-Matric Scholarship మరియు "
                    + "National Scholarship Portalలోని పథకాలను పరిశీలించండి. అర్హత, ఆదాయ పరిమితి, అవసరమైన పత్రాలు, "
                    + "చివరి తేదీ పథకాన్ని బట్టి మారుతాయి. అధికారిక వెబ్‌సైట్లలో తాజా వివరాలను ధృవీకరించండి: "
                    + "https://jnanabhumi.ap.gov.in/ మరియు https://scholarships.gov.in/";
        }



         
if (!telugu && isApplicationQuestion(message)) {
    String lowerMessage = message.toLowerCase(Locale.ROOT);

    if (lowerMessage.contains("pm-kisan")
            || lowerMessage.contains("pm kisan")
            || lowerMessage.contains("pradhan mantri kisan samman nidhi")) {
        return "For PM-KISAN, I don't have verified, step-by-step "
                + "application instructions or a confirmed document "
                + "checklist in my current information. Please check "
                + "the official portal for the latest process and "
                + "requirements: https://www.pmkisan.gov.in/";
    }

    if (lowerMessage.contains("post-matric")
            || lowerMessage.contains("post matric")) {
        return "For the Post-Matric Scholarship, the exact application "
                + "process and required documents depend on the applicable "
                + "scholarship and current official rules. Please check "
                + "the Andhra Pradesh Jnanabhumi portal for verified "
                + "instructions: https://jnanabhumi.ap.gov.in/";
    }

    if (lowerMessage.contains("national scholarship portal")
            || lowerMessage.matches(".*\\bnsp\\b.*")) {
        return "The application process and required documents depend "
                + "on the scholarship selected on the National Scholarship "
                + "Portal. Please consult its current official instructions: "
                + "https://scholarships.gov.in/";
    }

    return "I don't have verified, scheme-specific application steps "
            + "or a confirmed document checklist for your question. "
            + "Please share the exact scheme name so I can direct you "
            + "to the relevant official information.";
}


        if (telugu && isApplicationQuestion(message)) {
            String lowerMessage = message.toLowerCase(Locale.ROOT);

            if (lowerMessage.contains("pm-kisan")
                    || lowerMessage.contains("pm kisan")
                    || lowerMessage.contains("pradhan mantri kisan samman nidhi")) {
                return "PM-KISAN \u0c2a\u0c25\u0c15\u0c3e\u0c28\u0c3f\u0c15\u0c3f \u0c26\u0c30\u0c16\u0c3e\u0c38\u0c4d\u0c24\u0c41 \u0c1a\u0c47\u0c2f\u0c21\u0c3e\u0c28\u0c3f\u0c15\u0c3f, \u0c24\u0c3e\u0c1c\u0c3e \u0c35\u0c3f\u0c35\u0c30\u0c3e\u0c32\u0c41 \u0c2e\u0c30\u0c3f\u0c2f\u0c41 \u0c05\u0c35\u0c38\u0c30\u0c2e\u0c48\u0c28 \u0c2a\u0c24\u0c4d\u0c30\u0c3e\u0c32 \u0c15\u0c4b\u0c38\u0c02 \u0c05\u0c27\u0c3f\u0c15\u0c3e\u0c30\u0c3f\u0c15 \u0c35\u0c46\u0c2c\u0c4d\u200c\u0c38\u0c48\u0c1f\u0c4d \u0c1a\u0c42\u0c21\u0c02\u0c21\u0c3f: https://www.pmkisan.gov.in/";
            }

            return "I don't have verified, scheme-specific application steps for this question. Please share the exact scheme name so I can direct you to its official information.";
        }



                if (telugu && isScholarshipEligibilityQuestion(message)) {
            return "Post-Matric Scholarship \u0c15\u0c41 \u0c38\u0c02\u0c2c\u0c02\u0c27\u0c3f\u0c02\u0c1a\u0c3f\u0c28 \u0c16\u0c1a\u0c4d\u0c1a\u0c3f\u0c24\u0c2e\u0c48\u0c28 \u0c05\u0c30\u0c4d\u0c39\u0c24 \u0c2a\u0c4d\u0c30\u0c2e\u0c3e\u0c23\u0c3e\u0c32\u0c41 \u0c2a\u0c4d\u0c30\u0c38\u0c4d\u0c24\u0c41\u0c24 \u0c38\u0c2e\u0c3e\u0c1a\u0c3e\u0c30\u0c02\u0c32\u0c4b \u0c27\u0c43\u0c35\u0c40\u0c15\u0c30\u0c3f\u0c02\u0c1a\u0c2c\u0c21\u0c32\u0c47\u0c26\u0c41. "
                    + "\u0c05\u0c30\u0c4d\u0c39\u0c24 \u0c28\u0c3f\u0c2f\u0c2e\u0c3e\u0c32\u0c41 \u0c2a\u0c25\u0c15\u0c02 \u0c2e\u0c30\u0c3f\u0c2f\u0c41 \u0c30\u0c3e\u0c37\u0c4d\u0c1f\u0c4d\u0c30\u0c3e\u0c28\u0c4d\u0c28\u0c3f \u0c2c\u0c1f\u0c4d\u0c1f\u0c3f \u0c2e\u0c3e\u0c30\u0c35\u0c1a\u0c4d\u0c1a\u0c41. "
                    + "\u0c06\u0c02\u0c27\u0c4d\u0c30\u0c2a\u0c4d\u0c30\u0c26\u0c47\u0c36\u0c4d\u200c\u0c15\u0c41 \u0c38\u0c02\u0c2c\u0c02\u0c27\u0c3f\u0c02\u0c1a\u0c3f\u0c28 \u0c05\u0c27\u0c3f\u0c15\u0c3e\u0c30\u0c3f\u0c15 \u0c35\u0c3f\u0c35\u0c30\u0c3e\u0c32 \u0c15\u0c4b\u0c38\u0c02: https://jnanabhumi.ap.gov.in/";
        }
        // Handle English Post-Matric Scholarship eligibility without guessing.
        if (!telugu && isScholarshipEligibilityQuestion(message)) {
            String eligibilityMessage = message.toLowerCase(Locale.ROOT);

            if (eligibilityMessage.contains("post-matric")
                    || eligibilityMessage.contains("post matric")
                    || eligibilityMessage.contains("postmatric")) {
                return "The exact eligibility criteria for the Post-Matric Scholarship "
                        + "are not verified in the current information. Eligibility "
                        + "rules may vary by scheme and state. For Andhra Pradesh, "
                        + "check the official Jnanabhumi portal: "
                        + "https://jnanabhumi.ap.gov.in/";
            }
        }
        // 5. Build the language-specific system prompt.

        String selectedLanguage = telugu ? "Telugu" : "English";

        // Ask for clarification when an English scholarship eligibility question
        // does not identify a specific scholarship.
        boolean vagueScholarshipQuestion =
                normalizedMessage.contains("scholarship")
                && (normalizedMessage.contains("eligibility")
                    || normalizedMessage.contains("eligible")
                    || normalizedMessage.contains("income limit")
                    || normalizedMessage.contains("qualify")
                    || normalizedMessage.contains("criteria"));

        boolean namesSpecificScholarship =
                normalizedMessage.contains("post-matric")
                || normalizedMessage.contains("post matric")
                || normalizedMessage.contains("national scholarship portal")
                || normalizedMessage.contains("nsp")
                || normalizedMessage.contains("pm scholarship")
                || normalizedMessage.contains("merit scholarship");

        if (!telugu
                && (history == null || history.isEmpty())
                && vagueScholarshipQuestion
                && !namesSpecificScholarship) {
            return "Which scholarship do you mean? Please share its name, or tell me your course and state so I can help identify relevant options.";
        }

        String schemeContext = buildSchemeContext(schemes);
        System.out.println("[CivicSaathi DEBUG] Scheme catalogue records: "
                + schemes.stream().filter(scheme -> scheme != null).count());
        System.out.println("[CivicSaathi DEBUG] Scheme context characters: "
                + schemeContext.length());



        String systemPrompt = """

                You are CivicSaathi, an assistant for government schemes

                and public services.



                STRICT RESPONSE LANGUAGE:
                The selected response language is %s.
                This language requirement is mandatory for the entire answer.
                If the selected language is English:
                - Write every explanation in clear, simple English.
                - Do not write Telugu, Hindi, or any non-English sentences.
                - Keep official scheme names, acronyms, and URLs unchanged.
                If the selected language is Telugu:
                - Write explanations in natural, simple Telugu using Telugu script.
                - Do not use Hindi or Devanagari script.
                - Keep official scheme names, acronyms, and URLs unchanged.
                Never choose the response language from the user's message,
                catalogue text, or conversation history. Follow the selected
                response language above.



                ROLE AND CONSISTENCY:

                You are CivicSaathi, a digital assistant for government
                schemes and public services in India.

                When asked about your role, explain this purpose briefly.
                Do not invent an organization history, nonprofit status,
                target beneficiaries, partnerships, or other project details.
                Do not claim CivicSaathi serves only a particular group unless
                that information is explicitly provided.

                For greetings and simple general questions, respond naturally
                and briefly. Do not force every conversation into a scheme
                recommendation.

                Never produce an unrelated safety refusal for a harmless
                question about CivicSaathi or its role. If a request is
                genuinely outside your scope, explain your limitations briefly.

                Keep answers consistent with the supplied system instructions
                and verified catalogue. Do not contradict these instructions.
                ACCURACY:

                Use the scheme catalogue below for catalogue information.

                Never invent scheme names, benefits, eligibility criteria,

                required documents, deadlines or application steps.

                Do not claim that a scheme is in the catalogue unless it

                appears in the records below.

                Do not assume every person is eligible.

                If information is missing, say so in the requested language.

                Never invent an official URL.

                Distinguish general information from confirmed eligibility.
                Do not turn generic eligibility examples into confirmed rules.

                For a named scheme, use only eligibility details explicitly
                present in its catalogue record or other supplied verified context.

                If the catalogue gives only general eligibility information,
                clearly state that the exact criteria are not available here.

                Do not add citizenship, admission, income, category, attendance,
                or other requirements unless they are explicitly supported
                by the supplied scheme information.

                Do not invent website navigation steps. 
                For application procedure questions, provide only steps explicitly supported by the supplied verified scheme information.

Do not invent registration button names, form fields, OTP procedures, document upload requirements, or application steps.
                Treat every application procedure detail as unverified unless it appears explicitly in the supplied scheme catalogue or verified context.

                Do not generate numbered application steps from general knowledge or assumptions.

                Do not infer a registration process from the existence of an official website.

                If verified application steps are unavailable, explain that they are not available in the current information and direct the user to the official portal.

                If required documents are not explicitly listed in the supplied scheme information, state that the document list is not available in the current information. Do not guess which documents are required.

Do not list documents as mandatory unless the supplied scheme information explicitly confirms they are required.

When the application procedure is not available in the supplied information, provide the official website and explain that the user should check the current official instructions there.

Keep the answer concise and distinguish verified information from details that still need confirmation.
Provide only the
                official URL supplied in the catalogue when relevant.

                Ask the user to consult the official scheme guidelines
                for any missing or current eligibility conditions.



                CATEGORY RELEVANCE:

                For scholarship, student education, or college fee

                assistance questions, discuss only relevant education

                schemes from the catalogue. Never recommend farmer schemes

                such as PM-KISAN as scholarships.

                For farmer questions, discuss only relevant farmer schemes.

                Do not treat a general portal as proof that a specific

                scheme is available to every student.

                If the catalogue lacks enough information to answer, explain

                what is missing and direct the user to the relevant official

                portal already listed in the catalogue.



                CONVERSATION:

                Answer the latest question directly.

                Use relevant conversation history where appropriate.

                Avoid unnecessary repetition.

                Ask one concise follow-up question if essential information

                is missing.

                Keep the answer focused and understandable.

                RESPONSE LENGTH:
                For routine questions, prefer 3 to 5 short steps or bullet points.
                For routine questions, answer in at most 3 concise numbered steps.
Aim for 60 words or fewer when sufficient to answer correctly.
For eligibility questions, explain how to verify eligibility using the
relevant official scheme page. Do not invent scheme-specific criteria,
documents, or example schemes that are absent from the catalogue.
Ask one brief follow-up if the user wants personalized eligibility guidance.
                Include only information relevant to the user's question.
                Do not add generic eligibility requirements or unrelated websites
                as if they were verified scheme facts.
                Include essential qualifications and limitations when relevant.



                AVAILABLE SCHEME CATALOGUE:

                %s

                """.formatted(selectedLanguage, schemeContext);



        // 6. Prepare the conversation history.

        List<Map<String, String>> messages = new ArrayList<>();

        messages.add(createMessage("system", systemPrompt));



        if (history != null && !history.isEmpty()) {

            int start = Math.max(0, history.size() - 4);



            for (ChatController.ChatMessage item

                    : history.subList(start, history.size())) {



                if (item == null

                        || item.role() == null

                        || item.content() == null) {

                    continue;

                }



                String role = item.role().trim();

                String content = item.content().trim();



                if (!"user".equals(role)

                        && !"assistant".equals(role)) {

                    continue;

                }



                if (content.isEmpty()) {

                    continue;

                }



                // Avoid sending the current question twice.

                if ("user".equals(role)

                        && content.equals(message.trim())) {

                    continue;

                }



                // Do not let an earlier assistant answer override the selected language.
                if ("assistant".equals(role)
                        && ((telugu && containsDevanagari(content))
                        || (!telugu && containsTeluguScript(content)))) {
                    continue;
                }

                if (content.length() > 1200) {

                    content = content.substring(0, 1200);

                }



                messages.add(createMessage(role, content));

            }

        }



        messages.add(createMessage("user", message.trim()));



        // 7. Configure Ollama.

        Map<String, Object> options = new LinkedHashMap<>();

        options.put("num_predict", 300);

        options.put("temperature", 0.1);

        options.put("repeat_penalty", 1.15);



        Map<String, Object> request = new LinkedHashMap<>();

        request.put("model", model);

        request.put("messages", messages);

        request.put("stream", false);

        request.put("keep_alive", "10m");

        request.put("options", options);



        try {

            long ollamaStart = System.nanoTime();

String reply = callOllama(request);

long ollamaElapsed = System.nanoTime() - ollamaStart;

System.out.println("[CivicSaathi TIMING] Initial Ollama call: "
        + (ollamaElapsed / 1_000_000) + " ms");

System.out.println("[CivicSaathi DEBUG] Ollama reply: " + reply);


            // 8. Validate the response language and retry once if needed.
            boolean acceptable = telugu
                    ? isAcceptableTelugu(reply)
                    : isAcceptableEnglish(reply);

            if (!acceptable) {
                List<Map<String, String>> retryMessages =
                        new ArrayList<>(messages);

                String retryInstruction = telugu
                        ? "Rewrite the answer to the original question in natural, simple Telugu. "
                          + "Use Telugu script for explanations. Do not use Hindi or Devanagari. "
                          + "Keep official names and URLs unchanged. Use only catalogue facts. "
                          + "If information is unavailable, say so in Telugu."
                        : "Rewrite the answer to the original question in clear, simple English only. "
                          + "Do not use Telugu or Hindi sentences or scripts. "
                          + "Keep official names and URLs unchanged. Use only catalogue facts. "
                          + "If information is unavailable, say so in English.";

                retryMessages.add(createMessage("user", retryInstruction));

                Map<String, Object> retryRequest = new LinkedHashMap<>(request);
                retryRequest.put("messages", retryMessages);
                long retryStart = System.nanoTime();

reply = callOllama(retryRequest);

long retryElapsed = System.nanoTime() - retryStart;

System.out.println("[CivicSaathi TIMING] Language retry: "
        + (retryElapsed / 1_000_000) + " ms");

                acceptable = telugu
                        ? isAcceptableTelugu(reply)
                        : isAcceptableEnglish(reply);
            }

            // 9. Use a safe response if the model still violates the language choice.
                        if (!acceptable) {
                return telugu
                        ? "క్షమించండి, మీ ప్రశ్నకు సరైన తెలుగులో సమాధానం ఇవ్వలేకపోయాను. దయచేసి మళ్లీ ప్రయత్నించండి లేదా సంబంధిత అధికారిక ప్రభుత్వ వెబ్‌సైట్‌లో వివరాలను ధృవీకరించండి."
                        : "Sorry, I couldn't produce a reliable English answer just now. Please try again, or check the relevant official government website.";
            }

            return reply.trim();



        } catch (ResponseStatusException exception) {

            throw exception;



        } catch (RestClientException exception) {

            throw new ResponseStatusException(

                    HttpStatus.SERVICE_UNAVAILABLE,

                    "Cannot connect to Ollama. Check that Ollama is "

                    + "running and the configured model is available."

            );

        }

    }



    // Send a request to Ollama and extract the assistant response.

    private String callOllama(Map<String, Object> request) {



        Map<?, ?> response = restTemplate.postForObject(

                ollamaUrl + "/api/chat",

                request,

                Map.class

        );



        if (response == null

                || !(response.get("message") instanceof Map<?, ?>)) {

            throw new ResponseStatusException(

                    HttpStatus.BAD_GATEWAY,

                    "Ollama returned an empty response."

            );

        }



        Map<?, ?> assistantMessage =

                (Map<?, ?>) response.get("message");



        Object content = assistantMessage.get("content");



        if (!(content instanceof String reply) || reply.isBlank()) {

            throw new ResponseStatusException(

                    HttpStatus.BAD_GATEWAY,

                    "The AI returned an empty answer."

            );

        }



        return reply.trim();

    }



    private boolean isTelugu(String language) {

        if (language == null) {

            return false;

        }



        String normalized = language.trim().toLowerCase(Locale.ROOT);



        return normalized.equals("te")

                || normalized.equals("telugu")

                || normalized.startsWith("te-");

    }



    private boolean containsDevanagari(String text) {

        return text != null

                && text.matches("(?s).*[\u0900-\u097F].*");

    }



    // Basic script validation; this does not guarantee grammatical accuracy.

    private boolean isAcceptableTelugu(String text) {

        return text != null

                && !text.isBlank()

                && !containsDevanagari(text)

                && text.matches("(?s).*[\u0C00-\u0C7F].*");

    }



    private boolean containsTeluguScript(String text) {
        return text != null && text.matches("(?s).*[\\u0C00-\\u0C7F].*");
    }

    // English answers may contain official names and URLs, but not Telugu script.
    private boolean isAcceptableEnglish(String text) {
        return text != null
                && !text.isBlank()
                && !containsTeluguScript(text)
                && !containsDevanagari(text)
                && text.matches("(?s).*[A-Za-z].*");
    }

    private boolean isGeneralSchemeQuestion(String message) {

        String text = message.toLowerCase(Locale.ROOT);



        return text.contains("ప్రభుత్వ పథకాల గురించి తెలుసుకోవాలి")

                || text.contains("ప్రభుత్వ పథకాల గురించి చెప్పండి")

                || text.contains("ప్రభుత్వ పథకాల గురించి తెలుసుకోవాలనుకుంటున్నాను")

                || text.contains("ప్రభుత్వ పథకాలు ఏమిటి")

                || text.contains("government schemes");

    }



    private boolean isStudentSchemeQuestion(String message) {
        if (message == null || message.isBlank()) {
            return false;
        }

        String text = message.toLowerCase(Locale.ROOT);

        boolean educationTopic =
                text.contains("student")
                || text.contains("scholarship")
                || text.contains("\u0C35\u0C3F\u0C26\u0C4D\u0C2F");

        boolean schemeTopic =
                text.contains("scheme")
                || text.contains("available")
                || text.contains("scholarship")
                || text.contains("\u0C2A\u0C25\u0C15\u0C3E\u0C32");

        return educationTopic && schemeTopic;
    }



private boolean isEnglishEducationListQuestion(String message) {

    if (message == null || message.isBlank()) {

        return false;

    }



    String text = message.toLowerCase(Locale.ROOT);



    boolean asksForList =

            text.contains("what education schemes")

            || text.contains("which education schemes")

            || text.contains("education schemes available")

            || text.contains("scholarships available")
            || text.contains("what scholarships are available")

            || text.contains("scholarship schemes for students")

            || text.contains("education scholarships");



    boolean asksForDetails =

            text.contains("eligibility")

            || text.contains("how to apply")

            || text.contains("application process")

            || text.contains("required documents")

            || text.contains("documents required");



    return asksForList && !asksForDetails;

}





        private boolean isScholarshipEligibilityQuestion(String message) {
        if (message == null || message.isBlank()) {
            return false;
        }

        String text = message.toLowerCase(Locale.ROOT);

        boolean scholarshipTopic =
                text.contains("scholarship")
                || text.contains("scholarships")
                || text.contains("\u0c38\u0c4d\u0c15\u0c3e\u0c32\u0c30")
                || text.contains("\u0c35\u0c3f\u0c26\u0c4d\u0c2f\u0c3e\u0c30\u0c4d\u0c25\u0c3f");

        boolean eligibilityTopic =
                text.contains("eligible")
                || text.contains("eligibility")
                || text.contains("qualify")
                || text.contains("qualification")
                || text.contains("\u0c05\u0c30\u0c4d\u0c39");

        return scholarshipTopic && eligibilityTopic;
    }

    private boolean isApplicationQuestion(String message) {
    String text = message.toLowerCase(Locale.ROOT);

    return text.contains("apply")
            || text.contains("application")
            || text.contains("how to apply")
            || text.contains("how do i apply")
            || text.contains("how can i apply")
            || text.contains("documents required")
            || text.contains("required documents")
            || text.contains("which documents")
            || text.contains("what documents")
            || text.contains("document checklist")
            || text.contains("registration process")
            || text.contains("how to register")
            || text.contains("apply for this scheme")
            || text.contains("\u0C26\u0C30\u0C16\u0C3E\u0C38\u0C4D\u0C24\u0C41")
            || text.contains("\u0C05\u0C2A\u0C4D\u0C32\u0C48");
}



    private boolean isFarmerSchemeQuestion(String message) {

        String text = message.toLowerCase(Locale.ROOT);



        return text.contains("farmer")

                || text.contains("farmers")

                || text.contains("agriculture")

                || text.contains("రైతు")

                || text.contains("రైతుల")

                || text.contains("వ్యవసాయ")

                || text.contains("కిసాన్");

    }



    // Find only farmer-related records that really exist in the catalogue.

    private List<Scheme> findFarmerSchemes(List<Scheme> schemes) {

        if (schemes == null || schemes.isEmpty()) {

            return List.of();

        }



        return schemes.stream()

                .filter(scheme -> scheme != null)

                .filter(scheme -> {

                    String category = safe(scheme.getCategory())

                            .toLowerCase(Locale.ROOT);



                    String details = (

                            safe(scheme.getName()) + " "

                            + safe(scheme.getDescription()) + " "

                            + safe(scheme.getEligibility())

                    ).toLowerCase(Locale.ROOT);



                    return category.contains("agriculture")

                            || category.contains("farmer")

                            || details.contains("farmer")

                            || details.contains("agriculture")

                            || details.contains("agricultural")

                            || details.contains("kisan")

                            || details.contains("రైతు")

                            || details.contains("వ్యవసాయ")

                            || details.contains("కిసాన్");

                })

                .toList();

    }



private String buildEducationSchemeListResponse(List<Scheme> schemes) {

    List<Scheme> educationSchemes = schemes.stream()

            .filter(scheme -> scheme.getCategory() != null

                    && scheme.getCategory().equalsIgnoreCase("Education"))

            .toList();



    if (educationSchemes.isEmpty()) {

        return "Our current catalogue does not contain verified "

                + "education scheme records. Please check the official "

                + "National Scholarship Portal: https://scholarships.gov.in/";

    }



    StringBuilder response = new StringBuilder(

            "Here are the education schemes in our current catalogue:\n");



    for (Scheme scheme : educationSchemes) {

        response.append("\n- ")

                .append(safe(scheme.getName()))

                .append(": ")

                .append(safe(scheme.getDescription()))

                .append("\n");



        String website = safe(scheme.getOfficialWebsite());



        if (!website.equals("Not specified")) {

            response.append("Official website: ")

                    .append(website)

                    .append("\n");

        }

    }



    response.append("\nNote: Eligibility and application rules vary by scheme. ")

            .append("Please confirm the current requirements on its official website.");



    return response.toString();

}





    private String buildFarmerSchemeResponse(

            List<Scheme> farmerSchemes,

            boolean telugu) {



        if (farmerSchemes.isEmpty()) {

            return telugu

                    ? "ప్రస్తుతం మా పథకాల జాబితాలో రైతులకు సంబంధించిన "

                      + "ధృవీకరించిన పథకాల వివరాలు లేవు. అందువల్ల "

                      + "నిర్ధారణ లేని పథక వివరాలను చెప్పడం లేదు. "

                      + "తాజా సమాచారం కోసం సంబంధిత ప్రభుత్వ అధికారిక "

                      + "వెబ్‌సైట్‌ను పరిశీలించండి."

                    : "Our current scheme catalogue does not contain "

                      + "verified farmer-related scheme records. "

                      + "I won't invent scheme details. Please check "

                      + "the relevant official government website.";

        }



        StringBuilder response = new StringBuilder();



        response.append(telugu

                ? "మా పథకాల జాబితాలో ఉన్న రైతులకు సంబంధించిన పథకాలు:\n"

                : "Farmer-related schemes in our catalogue:\n");



        for (Scheme scheme : farmerSchemes) {

            response.append("\n- ")

                    .append(safe(scheme.getName()))

                    .append("\n");



            response.append(telugu ? "వివరణ: " : "Description: ")

                    .append(localizedDescription(scheme, telugu))

                    .append("\n");



            response.append(telugu ? "అర్హత వివరాలు: " : "Eligibility: ")

                    .append(localizedEligibility(scheme, telugu))

                    .append("\n");



            response.append(telugu

                    ? "అవసరమైన పత్రాలు: "

                    : "Required documents: ")

                    .append(localizedDocuments(scheme, telugu))

                    .append("\n");



            String website = safe(scheme.getOfficialWebsite());



            if (!website.equals("Not specified")) {

                response.append(telugu

                        ? "అధికారిక వెబ్‌సైట్: "

                        : "Official website: ")

                        .append(website)

                        .append("\n");

            }

        }



        response.append(telugu

                ? "\nగమనిక: ప్రస్తుత అర్హతలు, ప్రయోజనాలు, దరఖాస్తు "

                  + "నిబంధనలను అధికారిక వెబ్‌సైట్‌లో నిర్ధారించండి."

                : "\nNote: Confirm current eligibility, benefits and "

                  + "application rules on the official website.");



        return response.toString();

    }



    // Translate known PM-KISAN details into Telugu.

    // Keep the original official scheme name unchanged.

    private String localizedDescription(Scheme scheme, boolean telugu) {

        if (!telugu) {

            return safe(scheme.getDescription());

        }



        String name = safe(scheme.getName()).toLowerCase(Locale.ROOT);



        if (isPmKisan(name)) {

            return "ఈ పథకం ద్వారా అర్హత కలిగిన భూమి కలిగిన రైతు "

                    + "కుటుంబాలకు సంవత్సరానికి రూ. 6,000 ఆర్థిక "

                    + "సహాయం మూడు సమాన విడతల్లో అందిస్తారు. "

                    + "ఈ సహాయం పథకం నిబంధనలు మరియు అర్హతలకు లోబడి ఉంటుంది.";

        }



        return "ఈ పథకం వివరణ తెలుగులో ఇంకా అందుబాటులో లేదు. "

                + "వివరాల కోసం అధికారిక వెబ్‌సైట్‌ను పరిశీలించండి.";

    }



    private String localizedEligibility(Scheme scheme, boolean telugu) {

        if (!telugu) {

            return safe(scheme.getEligibility());

        }



        String name = safe(scheme.getName()).toLowerCase(Locale.ROOT);



        if (isPmKisan(name)) {

            return "భూమి రికార్డులు, ప్రస్తుత పథక నిబంధనలు మరియు "

                    + "వర్తించే మినహాయింపుల ఆధారంగా అర్హత నిర్ణయిస్తారు. "

                    + "ప్రతి రైతు ఈ పథకానికి అర్హుడు కాదు. మీ అర్హతను "

                    + "అధికారిక పోర్టల్‌లో నిర్ధారించండి.";

        }



        return "ఈ పథకానికి సంబంధించిన అర్హత వివరాలు తెలుగులో "

                + "అందుబాటులో లేవు. అధికారిక వెబ్‌సైట్‌లో "

                + "ప్రస్తుత నిబంధనలను నిర్ధారించండి.";

    }



    private String localizedDocuments(Scheme scheme, boolean telugu) {

        List<String> documents = scheme.getRequiredDocuments();



        if (!telugu) {

            return documents == null || documents.isEmpty()

                    ? "Not specified"

                    : documents.stream()

                            .map(ChatService::safe)

                            .collect(Collectors.joining(", "));

        }



        String name = safe(scheme.getName()).toLowerCase(Locale.ROOT);



        if (isPmKisan(name)) {

            return "ఆధార్ వివరాలు, భూమి రికార్డులు, బ్యాంకు ఖాతా "

                    + "వివరాలు మరియు అధికారిక పోర్టల్ సూచించే ఇతర "

                    + "వివరాలు అవసరం కావచ్చు. నమోదు చేసుకునే ముందు "

                    + "ప్రస్తుత అధికారిక సూచనలను పరిశీలించండి.";

        }



        return "అవసరమైన పత్రాల వివరాలు తెలుగులో అందుబాటులో లేవు. "

                + "అధికారిక వెబ్‌సైట్‌లో పత్రాల జాబితాను పరిశీలించండి.";

    }



    private boolean isPmKisan(String name) {

        return name.contains("pm-kisan")

                || name.contains("pm kisan")

                || name.contains("pradhan mantri kisan samman nidhi");

    }



    // Build model context from the current scheme catalogue.

    private String buildSchemeContext(List<Scheme> schemes) {

        if (schemes == null || schemes.isEmpty()) {

            return "No scheme records are currently available. "

                    + "Do not invent catalogue entries.";

        }



        return schemes.stream()

                .filter(scheme -> scheme != null)

                .map(scheme -> """

                        Scheme name: %s

                        Category: %s

                        Description: %s

                        Eligibility information: %s

                        Required documents listed in the catalogue: %s

                        Official website listed in the catalogue: %s

                        """.formatted(

                        safe(scheme.getName()),

                        safe(scheme.getCategory()),

                        safe(scheme.getDescription()),

                        safe(scheme.getEligibility()),

                        scheme.getRequiredDocuments() == null

                                || scheme.getRequiredDocuments().isEmpty()

                                ? "Not specified"

                                : scheme.getRequiredDocuments().stream()

                                        .map(ChatService::safe)

                                        .collect(Collectors.joining(", ")),

                        safe(scheme.getOfficialWebsite())

                ))

                .collect(Collectors.joining("\n"));

    }



    private static String safe(Object value) {

        if (value == null) {

            return "Not specified";

        }



        String text = value.toString().trim();



        return text.isEmpty() ? "Not specified" : text;

    }



    private Map<String, String> createMessage(

            String role,

            String content) {



        Map<String, String> item = new LinkedHashMap<>();

        item.put("role", role);

        item.put("content", content);



        return item;

    }

}