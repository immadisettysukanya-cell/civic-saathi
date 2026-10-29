import { useEffect, useMemo, useState } from "react";

import "./App.css";
import AuthPage from "./AuthPage";



const API = "/api";




async function getCsrfHeaders() {
  return {};
}
const COPY = {

  en: {

    brand: "CivicSaathi",

    tagline: "Government services, made simpler.",

    dashboard: "Overview",

    schemes: "Explore schemes",

    assistant: "AI assistant",

    saved: "Saved schemes",

    help: "Your benefits, made easier",

    welcome: "Good day!",

    heroTitle: "Find support that",

    heroAccent: "fits your life.",

    heroText:

      "Discover government schemes, understand eligibility, and find your next step—all in one place.",

    explore: "Explore schemes",

    check: "Check my eligibility",

    search: "Search schemes, scholarships, benefits...",

    popular: "Explore by category",

    recommended: "Schemes to explore",

    seeAll: "View all schemes",

    featured: "Your next step starts here",

    featuredText:

      "Answer a few simple questions to discover schemes you may qualify for.",

    categories: ["All", "Education", "Agriculture", "Healthcare", "Housing", "Employment"],

    allSchemes: "Explore government schemes",

    allSchemesText: "Find benefits and services that may be relevant to you.",

    chatTitle: "Your personal guide",

    chatSub: "Ask about schemes, eligibility, and application steps.",

    chatWelcome:

      "Namaste! I'm your CivicSaathi guide. I can help you explore schemes or start an eligibility check. What would you like help with?",

    chatPlaceholder: "Ask about a scheme or benefit...",

    send: "Send",

    tryAsking: "Try asking",

    prompts: [

      "Find scholarships for me",

      "How do I check eligibility?",

      "Show education schemes",

    ],

    savedTitle: "Your saved schemes",

    savedText: "Keep useful schemes in one place for easy access.",

    emptySaved: "No saved schemes yet",

    emptySavedText: "Save a scheme with the bookmark icon and it will appear here.",

    noSchemes: "No matching schemes found",

    noSchemesText: "Try a different search or category.",

    save: "Save",

    savedLabel: "Saved",

    details: "View details",

    official: "Official website",

    checkEligibility: "Check eligibility",

    start: "Let's get started",

    profileTitle: "A few details first",

    profileText:

      "We'll use these details to ask relevant questions. You can change your answers as you go.",

    state: "State",

    education: "Current education level",

    continue: "Continue",

    cancel: "Cancel",

    back: "Back",

    questionOf: "Question",

    of: "of",

    chooseOption: "Choose an option below",

    enterNumber: "Enter a number",

    loading: "Loading...",

    resultTitle: "Your eligibility overview",

    potential: "Potential match",

    needsInfo: "Needs more information",

    notMatched: "Conditions not matched",

    matched: "Conditions that matched",

    unmet: "Conditions needing review",

    missing: "Information still needed",

    restart: "Start again",

    close: "Close",

    disclaimer:

      "This is a preliminary guide, not an official approval. Eligibility rules may change; the relevant department makes the final decision.",

    apiWarning:

      "The backend couldn't be reached. Start Spring Boot on port 8088 and try again.",

    retry: "Try again",

    footer: "Helping people find public services with more confidence.",

    online: "Service connected",

    offline: "Service unavailable",

    step: "Step-by-step guidance",

    verifiedNote: "Always confirm details on the official portal.",

    noDescription: "Open the scheme to learn more about its benefits and requirements.",

    income: "Income and household support",

    learning: "Education and scholarships",

    farming: "Agriculture and farmers",

    health: "Health and wellbeing",

    home: "Housing and essentials",

    work: "Employment and skills",

    status: "Status",

    assistantThinking: "Checking available information...",

    chatHelp:

      "I can search the available scheme list or help you start the eligibility questionnaire.",

  },

  te: {

    brand: "సివిక్ సాథీ",

    tagline: "ప్రభుత్వ సేవలు ఇప్పుడు సులభం.",

    dashboard: "అవలోకనం",

    schemes: "పథకాలను చూడండి",

    assistant: "AI సహాయకుడు",

    saved: "సేవ్ చేసిన పథకాలు",

    help: "మీకు ఉపయోగపడే ప్రయోజనాలు",

    welcome: "నమస్కారం!",

    heroTitle: "మీకు సరిపోయే",

    heroAccent: "సహాయాన్ని కనుగొనండి.",

    heroText:

      "ప్రభుత్వ పథకాలను తెలుసుకోండి, అర్హతను అర్థం చేసుకోండి, తదుపరి చర్యను కనుగొనండి.",

    explore: "పథకాలను చూడండి",

    check: "నా అర్హతను తెలుసుకోండి",

    search: "పథకాలు, స్కాలర్‌షిప్‌లు వెతకండి...",

    popular: "వర్గాల వారీగా చూడండి",

    recommended: "మీరు పరిశీలించగల పథకాలు",

    seeAll: "అన్ని పథకాలు",

    featured: "మీ తదుపరి అడుగు ఇక్కడ ప్రారంభం",

    featuredText:

      "మీకు సరిపోయే పథకాలను కనుగొనడానికి కొన్ని ప్రశ్నలకు సమాధానమివ్వండి.",

    categories: ["అన్నీ", "విద్య", "వ్యవసాయం", "ఆరోగ్యం", "గృహవసతి", "ఉపాధి"],

    allSchemes: "ప్రభుత్వ పథకాలను అన్వేషించండి",

    allSchemesText: "మీకు ఉపయోగపడే పథకాలు, సేవలను కనుగొనండి.",

    chatTitle: "మీ వ్యక్తిగత సహాయకుడు",

    chatSub: "పథకాలు, అర్హత, దరఖాస్తు విధానం గురించి అడగండి.",

    chatWelcome:

      "నమస్కారం! నేను మీ CivicSaathi సహాయకుడిని. పథకాలను వెతకడంలో లేదా అర్హతను తెలుసుకోవడంలో సహాయం చేస్తాను. మీకు ఏ సహాయం కావాలి?",

    chatPlaceholder: "పథకం లేదా ప్రయోజనం గురించి అడగండి...",

    send: "పంపండి",

    tryAsking: "ఇలా అడగండి",

    prompts: [

      "నాకు స్కాలర్‌షిప్‌లు కావాలి",

      "అర్హతను ఎలా తెలుసుకోవాలి?",

      "విద్యా పథకాలను చూపించండి",

    ],

    savedTitle: "మీరు సేవ్ చేసిన పథకాలు",

    savedText: "మీకు ఉపయోగపడే పథకాలను ఇక్కడ ఉంచుకోండి.",

    emptySaved: "ఇంకా పథకాలు సేవ్ చేయలేదు",

    emptySavedText: "పథకంలోని బుక్‌మార్క్ చిహ్నాన్ని ఎంచుకుంటే ఇక్కడ కనిపిస్తుంది.",

    noSchemes: "సరిపోలే పథకాలు కనబడలేదు",

    noSchemesText: "మరొక పదం లేదా వర్గంతో వెతకండి.",

    save: "సేవ్ చేయండి",

    savedLabel: "సేవ్ చేశారు",

    details: "వివరాలు చూడండి",

    official: "అధికారిక వెబ్‌సైట్",

    checkEligibility: "అర్హతను తెలుసుకోండి",

    start: "ప్రారంభిద్దాం",

    profileTitle: "ముందుగా కొన్ని వివరాలు",

    profileText: "మీకు సంబంధించిన ప్రశ్నలను చూపించడానికి ఈ వివరాలు ఉపయోగపడతాయి.",

    state: "రాష్ట్రం",

    education: "ప్రస్తుత విద్యా స్థాయి",

    continue: "కొనసాగించండి",

    cancel: "రద్దు చేయండి",

    back: "వెనక్కి",

    questionOf: "ప్రశ్న",

    of: "/",

    chooseOption: "క్రింద ఒక ఎంపికను ఎంచుకోండి",

    enterNumber: "సంఖ్యను నమోదు చేయండి",

    loading: "లోడ్ అవుతోంది...",

    resultTitle: "మీ అర్హత వివరాలు",

    potential: "అర్హత ఉండవచ్చు",

    needsInfo: "మరింత సమాచారం అవసరం",

    notMatched: "కొన్ని షరతులు సరిపోలలేదు",

    matched: "సరిపోయిన షరతులు",

    unmet: "పరిశీలించాల్సిన షరతులు",

    missing: "ఇంకా అవసరమైన సమాచారం",

    restart: "మళ్లీ ప్రారంభించండి",

    close: "మూసివేయండి",

    disclaimer:

      "ఇది ప్రాథమిక మార్గదర్శకం మాత్రమే; అధికారిక ఆమోదం కాదు. తుది నిర్ణయం సంబంధిత శాఖదే.",

    apiWarning:

      "బ్యాకెండ్‌కు కనెక్ట్ కాలేకపోయాము. Spring Boot‌ను 8088 పోర్ట్‌లో ప్రారంభించి మళ్లీ ప్రయత్నించండి.",

    retry: "మళ్లీ ప్రయత్నించండి",

    footer: "ప్రభుత్వ సేవలను సులభంగా కనుగొనడంలో మీకు తోడుగా.",

    online: "సేవ కనెక్ట్ అయింది",

    offline: "సేవ అందుబాటులో లేదు",

    step: "దశలవారీ మార్గదర్శకం",

    verifiedNote: "వివరాలను అధికారిక పోర్టల్‌లో తప్పనిసరిగా నిర్ధారించండి.",

    noDescription: "ప్రయోజనాలు, అవసరాలను తెలుసుకోవడానికి పథక వివరాలను చూడండి.",

    income: "ఆదాయం మరియు కుటుంబ సహాయం",

    learning: "విద్య మరియు స్కాలర్‌షిప్‌లు",

    farming: "వ్యవసాయం మరియు రైతులు",

    health: "ఆరోగ్యం",

    home: "గృహవసతి",

    work: "ఉపాధి మరియు నైపుణ్యాలు",

    status: "స్థితి",

    assistantThinking: "అందుబాటులో ఉన్న సమాచారాన్ని పరిశీలిస్తోంది...",

    chatHelp: "అందుబాటులో ఉన్న పథకాల జాబితాను వెతకగలను లేదా అర్హత ప్రశ్నావళిని ప్రారంభించగలను.",

  },

};



const FALLBACK_SCHEMES = [

  {

    id: "student-scholarships",

    name: "Student Scholarship Support",

    category: "Education",

    description: "Explore scholarship and fee-support options for eligible students.",

    officialWebsite: "https://jnanabhumi.ap.gov.in/",

  },

  {

    id: "national-scholarships",

    name: "National Scholarship Portal",

    category: "Education",

    description: "Find central and other listed scholarship opportunities.",

    officialWebsite: "https://scholarships.gov.in/",

  },

  {

    id: "farmer-support",

    name: "Farmer Support Schemes",

    category: "Agriculture",

    description: "Explore available public information about farmer support programmes.",

    officialWebsite: "https://www.india.gov.in/",

  },

  {

    id: "public-health",

    name: "Public Health Benefits",

    category: "Healthcare",

    description: "Explore government health programmes and official information.",

    officialWebsite: "https://www.india.gov.in/",

  },

];



const QUESTION_LABELS = {

  annualFamilyIncome: {

    en: "What is your approximate annual family income?",

    te: "మీ కుటుంబ వార్షిక ఆదాయం సుమారుగా ఎంత?",

  },

  socialCategory: {

    en: "Which social category applies to you?",

    te: "మీకు వర్తించే సామాజిక వర్గం ఏది?",

  },

  collegeType: {

    en: "What type of college do you attend?",

    te: "మీరు ఏ రకమైన కళాశాలలో చదువుతున్నారు?",

  },

  studyMode: {

    en: "What is your study mode?",

    te: "మీ చదువు విధానం ఏది?",

  },

  admissionQuota: {

    en: "Which admission quota applies to you?",

    te: "మీ ప్రవేశ కోటా ఏది?",

  },

  attendancePercentage: {

    en: "What is your approximate attendance percentage?",

    te: "మీ హాజరు శాతం సుమారుగా ఎంత?",

  },

  governmentEmployeeOrPensioner: {

    en: "Is anyone in your family a government employee or pensioner?",

    te: "మీ కుటుంబంలో ఎవరైనా ప్రభుత్వ ఉద్యోగి లేదా పెన్షనర్‌గా ఉన్నారా?",

  },

  ownsFourWheeler: {

    en: "Does your family own a four-wheeler?",

    te: "మీ కుటుంబానికి నాలుగు చక్రాల వాహనం ఉందా?",

  },

  familyIncomeTaxPayer: {

    en: "Is anyone in your family an income-tax payer?",

    te: "మీ కుటుంబంలో ఎవరైనా ఆదాయపు పన్ను చెల్లిస్తున్నారా?",

  },

  wetLandAcres: {

    en: "How many acres of wet agricultural land does your family own? Enter 0 if none.",

    te: "మీ కుటుంబానికి ఎన్ని ఎకరాల తడి వ్యవసాయ భూమి ఉంది? లేకపోతే 0 నమోదు చేయండి.",

  },

  dryLandAcres: {

    en: "How many acres of dry agricultural land does your family own? Enter 0 if none.",

    te: "మీ కుటుంబానికి ఎన్ని ఎకరాల మెట్ట భూమి ఉంది? లేకపోతే 0 నమోదు చేయండి.",

  },

  urbanBuiltUpSqFt: {

    en: "What is the built-up area of your family's urban property in square feet? Enter 0 if none.",

    te: "మీ కుటుంబ పట్టణ ఆస్తి నిర్మిత విస్తీర్ణం ఎన్ని చదరపు అడుగులు? లేకపోతే 0 నమోదు చేయండి.",

  },

};



const OPTION_TRANSLATIONS = {

  "Government": "ప్రభుత్వ",

  "Aided": "ఎయిడెడ్",

  "Private affiliated": "ప్రైవేట్ అనుబంధ",

  "Private/deemed university": "ప్రైవేట్ / డీమ్డ్ విశ్వవిద్యాలయం",

  "Not sure": "తెలియదు",

  "Regular/full-time": "రెగ్యులర్ / పూర్తి సమయం",

  "Distance/correspondence": "దూరవిద్య / కరస్పాండెన్స్",

  "Regular/convener quota": "రెగ్యులర్ / కన్వీనర్ కోటా",

  "Management quota": "మేనేజ్‌మెంట్ కోటా",

  "Spot admission": "స్పాట్ అడ్మిషన్",

  "Other": "ఇతర",

  "Prefer not to say": "చెప్పదలచుకోలేదు",

  "Differently abled": "దివ్యాంగులు",

  "Minority": "మైనారిటీ",

  "Yes": "అవును",

  "No": "కాదు",

};



function normaliseScheme(item, index) {

  return {

    id: String(item.id ?? item.schemeId ?? item.name ?? item.schemeName ?? index),

    name: item.name ?? item.schemeName ?? item.title ?? `Government Scheme ${index + 1}`,

    category: item.category ?? item.schemeCategory ?? "Government Scheme",

    description:

      item.description ??

      item.shortDescription ??

      item.benefits ??

      "Open the scheme to learn more about its benefits and requirements.",

    officialWebsite:

      item.officialWebsite ?? item.website ?? item.officialUrl ?? item.url ?? "",

    eligibilityCriteria: item.eligibilityCriteria ?? item.eligibility ?? "",

  };

}



function getSchemeUrl(scheme) {

  const value = scheme.officialWebsite;

  return typeof value === "string" && /^https?:\/\//i.test(value) ? value : "";

}



function Icon({ name }) {

  const paths = {

    home: <><path d="m3 10 9-7 9 7" /><path d="M5 9v11h14V9" /><path d="M9 20v-6h6v6" /></>,

    search: <><circle cx="10.5" cy="10.5" r="6.5" /><path d="m16 16 5 5" /></>,

    chat: <><path d="M21 11.5a8.4 8.4 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.4 8.4 0 0 1-3.8-.9L3 21l1.9-5.7a8.4 8.4 0 0 1-.9-3.8A8.5 8.5 0 0 1 8.7 3.9a8.4 8.4 0 0 1 3.8-.9h.5a8.5 8.5 0 0 1 8 8z" /></>,

    bookmark: <><path d="M6 4h12v17l-6-4-6 4z" /></>,

    arrow: <><path d="M5 12h14" /><path d="m13 6 6 6-6 6" /></>,

    sparkle: <><path d="m12 3 1.9 5.8L20 11l-6.1 2.2L12 19l-1.9-5.8L4 11l6.1-2.2z" /><path d="m19 14 1.1 2.9L23 18l-2.9 1.1L19 22l-1.1-2.9L15 18l2.9-1.1z" /></>,

    check: <><path d="m5 12 4 4L19 6" /></>,

    external: <><path d="M14 3h7v7" /><path d="m10 14 11-11" /><path d="M19 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7a2 2 0 0 1 2-2h6" /></>,

    close: <><path d="m18 6-12 12" /><path d="m6 6 12 12" /></>,

    back: <><path d="M19 12H5" /><path d="m12 19-7-7 7-7" /></>,

    send: <><path d="m22 2-7 20-4-9-9-4z" /><path d="M22 2 11 13" /></>,

    leaf: <><path d="M20 4c-8 0-14 4-14 11a5 5 0 0 0 5 5c7 0 11-6 9-16Z" /><path d="M4 21c2-5 6-8 11-11" /></>,

    shield: <><path d="M12 22s8-4 8-11V5l-8-3-8 3v6c0 7 8 11 8 11z" /><path d="m9 12 2 2 4-4" /></>,

    menu: <><path d="M4 6h16" /><path d="M4 12h16" /><path d="M4 18h16" /></>,

  };

  return (

    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8"

      strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">

      {paths[name] ?? paths.sparkle}

    </svg>

  );

}



function App() {
  const [userEmail, setUserEmail] = useState("Guest");

  const [lang, setLang] = useState("en");
  const [settingsOpen, setSettingsOpen] = useState(false);
  const [textSize, setTextSize] = useState(
    () => localStorage.getItem("civicsaathi-text-size") || "medium"
  );
  const [textColor, setTextColor] = useState(
    () => localStorage.getItem("civicsaathi-text-color") || "#203b31"
  );

  const t = COPY[lang];

  useEffect(() => {
    localStorage.setItem("civicsaathi-text-size", textSize);
    localStorage.setItem("civicsaathi-text-color", textColor);
  }, [textSize, textColor]);

  const [page, setPage] = useState("dashboard");

  const [schemes, setSchemes] = useState(FALLBACK_SCHEMES);

  const [apiOnline, setApiOnline] = useState(false);

  const [search, setSearch] = useState("");

  const [category, setCategory] = useState("All");

  const [savedIds, setSavedIds] = useState(() => {

    try {

      return JSON.parse(localStorage.getItem("civicsaathi-saved") || "[]");

    } catch {

      return [];

    }

  });



 const [chatInput, setChatInput] = useState("");

const [messages, setMessages] = useState([]);

const [chatLoading, setChatLoading] = useState(false);

  const [wizardOpen, setWizardOpen] = useState(false);

  const [profile, setProfile] = useState({

    state: "Andhra Pradesh",

    educationLevel: "Undergraduate",

  });

  const [question, setQuestion] = useState(null);

  const [answers, setAnswers] = useState({});

  const [draft, setDraft] = useState("");

  const [result, setResult] = useState(null);

  const [wizardLoading, setWizardLoading] = useState(false);

  const [error, setError] = useState("");



  useEffect(() => {

    let active = true;

    fetch(`${API}/schemes`)

      .then((response) => {

        if (!response.ok) throw new Error("Could not load schemes");

        return response.json();

      })

      .then((data) => {

        if (!active) return;

        const list = Array.isArray(data)

          ? data

          : Array.isArray(data?.schemes)

            ? data.schemes

            : Array.isArray(data?.content)

              ? data.content

              : [];

        if (list.length) setSchemes(list.map(normaliseScheme));

        setApiOnline(true);

      })

      .catch(() => {

        if (active) setApiOnline(false);

      });

    return () => {

      active = false;

    };

  }, []);



  useEffect(() => {

    try {

      localStorage.setItem("civicsaathi-saved", JSON.stringify(savedIds));

    } catch {

      // The app remains usable if browser storage is unavailable.

    }

  }, [savedIds]);



  useEffect(() => {

    if (messages.length === 0) {

      setMessages([{ role: "assistant", text: t.chatWelcome, starter: true }]);

    } else {

      setMessages((current) =>

        current.map((message) =>

          message.starter && message.role === "assistant"

            ? { ...message, text: t.chatWelcome }

            : message

        )

      );

    }

    // Update only the initial assistant greeting when language changes.

    // eslint-disable-next-line react-hooks/exhaustive-deps

  }, [lang]);



  const filteredSchemes = useMemo(() => {

    const term = search.trim().toLowerCase();

    return schemes.filter((scheme) => {

      const matchesSearch =

        !term ||

        `${scheme.name} ${scheme.category} ${scheme.description}`

          .toLowerCase()

          .includes(term);

      const matchesCategory =

        category === "All" ||

        scheme.category.toLowerCase().includes(category.toLowerCase());

      return matchesSearch && matchesCategory;

    });

  }, [schemes, search, category]);



  const savedSchemes = useMemo(

    () => schemes.filter((scheme) => savedIds.includes(scheme.id)),

    [schemes, savedIds]

  );



  function navigate(nextPage) {

    setPage(nextPage);

    setSearch("");

    if (nextPage === "assistant" && messages.length === 0) {

      setMessages([{ role: "assistant", text: t.chatWelcome, starter: true }]);

    }

  }



  function toggleSaved(id) {

    setSavedIds((current) =>

      current.includes(id) ? current.filter((item) => item !== id) : [...current, id]

    );

  }



  async function loadNextQuestion(currentAnswers) {

    setWizardLoading(true);

    setError("");

    try {

      const response = await fetch(`${API}/eligibility/next-question`, {

        method: "POST",

        credentials: "include",
        headers: { "Content-Type": "application/json", ...(await getCsrfHeaders()) },

        body: JSON.stringify({

          state: profile.state,

          educationLevel: profile.educationLevel,

          answers: currentAnswers,

        }),

      });

      if (!response.ok) throw new Error("Unable to load the next question.");

      const data = await response.json();

      if (data.field === "complete") {

        await submitEligibility(currentAnswers);

        return;

      }

      setQuestion(data);

      setDraft(currentAnswers[data.field] ?? "");

    } catch {

      setError(t.apiWarning);

    } finally {

      setWizardLoading(false);

    }

  }



  async function submitEligibility(finalAnswers) {

    const response = await fetch(`${API}/eligibility/submit`, {

      method: "POST",

      credentials: "include",
        headers: { "Content-Type": "application/json", ...(await getCsrfHeaders()) },

      body: JSON.stringify({

        state: profile.state,

        educationLevel: profile.educationLevel,

        answers: finalAnswers,

      }),

    });

    if (!response.ok) throw new Error("Unable to calculate your eligibility overview.");

    const data = await response.json();

    setResult(data);

    setQuestion(null);

  }



  function startEligibility() {

    setWizardOpen(true);

    setAnswers({});

    setDraft("");

    setQuestion(null);

    setResult(null);

    setError("");

  }



  function beginQuestions() {

    loadNextQuestion({});

  }



  async function continueQuestion() {

    if (!question) return;

    const value = String(draft ?? "").trim();

    if (!value) {

      setError(lang === "te" ? "దయచేసి సమాధానం ఎంచుకోండి లేదా నమోదు చేయండి." : "Please choose or enter an answer.");

      return;

    }

    if (question.inputType === "number" && (!Number.isFinite(Number(value)) || Number(value) < 0)) {

      setError(lang === "te" ? "సున్నా లేదా అంతకంటే ఎక్కువ సంఖ్య నమోదు చేయండి." : "Enter a valid number greater than or equal to zero.");

      return;

    }

    const updated = { ...answers, [question.field]: value };

    setAnswers(updated);

    await loadNextQuestion(updated);

  }



  async function goBackQuestion() {

    const keys = Object.keys(answers);

    if (!keys.length) {

      setQuestion(null);

      return;

    }

    const previous = { ...answers };

    delete previous[keys[keys.length - 1]];

    setAnswers(previous);

    await loadNextQuestion(previous);

  }



  function closeWizard() {

    setWizardOpen(false);

    setQuestion(null);

    setResult(null);

    setError("");

  }



  function retryConnection() {

    setError("");

    if (question) loadNextQuestion(answers);

    else if (!result) beginQuestions();

  }



  function categoryValue(label) {
    const translatedCategories = {
      "అన్నీ": "All",
      "విద్య": "Education",
      "వ్యవసాయం": "Agriculture",
      "ఆరోగ్యం": "Healthcare",
      "గృహవసతి": "Housing",
      "ఉపాధి": "Employment",
    };

    return translatedCategories[label] ?? label;
  }

  async function sendChat(text = chatInput) {
    const message = text.trim();
    if (!message || chatLoading) return;

    const previousMessages = messages.filter(
      (item) =>
        !item.starter &&
        (item.role === "user" || item.role === "assistant") &&
        typeof item.text === "string"
    );

    setMessages((current) => [...current, { role: "user", text: message }]);
    setChatInput("");
    setChatLoading(true);

    try {
      const history = previousMessages
        .filter((item) => item.text.trim())
        .slice(-10)
        .map((item) => ({
          role: item.role,
          content: item.text,
        }));

      const response = await fetch(`${API}/chat`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json", ...(await getCsrfHeaders()) },
        body: JSON.stringify({
          message,
          language: lang === "te" ? "te" : "en",
          history,
        }),
      });

      if (!response.ok) {
        const details = await response.text();
        throw new Error(`Chat request failed (${response.status}). ${details}`);
      }

      const data = await response.json();
      if (!data.reply || typeof data.reply !== "string") {
        throw new Error("The AI returned an empty response.");
      }
      const eligibilityQuestion =
      /\b(eligib|scholarship|post[- ]matric|financial aid|student support)\b/i.test(message) ||
      /అర్హత|స్కాలర్.?షిప్|విద్యార్థి సహాయం/.test(message);
      setMessages((current) => [
        ...current,
        {
          role: "assistant",
          text: data.reply,
          ...(eligibilityQuestion ? { action: "eligibility" } : {}),
        },
      ]);

    } catch (error) {
      console.error("CivicSaathi chat error:", error);
      setMessages((current) => [
        ...current,
        {
          role: "assistant",
          text:
            lang === "te"
              ? "క్షమించండి, AI సహాయకుడిని సంప్రదించలేకపోయాము. Ollama మరియు Spring Boot నడుస్తున్నాయో చూసి మళ్లీ ప్రయత్నించండి."
              : "Sorry, I couldn't reach the AI assistant. Please check that Ollama and Spring Boot are running, then try again.",
          error: true,
        },
      ]);
    } finally {
      setChatLoading(false);
    }
  }

  function renderSchemeCard(scheme, index = 0) {

    const saved = savedIds.includes(scheme.id);

    const url = getSchemeUrl(scheme);

    const symbols = ["✳", "⌂", "◈", "✺", "◎", "◇"];

    return (

      <article className="scheme-card" key={scheme.id} style={{ "--card-index": index }}>

        <div className="scheme-card-top">

          <div className={`scheme-symbol scheme-symbol-${index % 5}`}>{symbols[index % symbols.length]}</div>

          <button

            className={`bookmark-button ${saved ? "is-saved" : ""}`}

            onClick={() => toggleSaved(scheme.id)}

            aria-label={saved ? "Remove saved scheme" : "Save scheme"}

            title={saved ? t.savedLabel : t.save}

          >

            <Icon name="bookmark" />

          </button>

        </div>

        <span className="category-label">{scheme.category}</span>

        <h3>{scheme.name}</h3>

        <p>{scheme.description || t.noDescription}</p>

        <div className="scheme-card-actions">

          <button className="text-action" onClick={() => {

            setMessages((current) => [

              ...current,

              { role: "assistant", text: `${scheme.name}\n\n${scheme.description || t.noDescription}${url ? `\n\n${t.official}: ${url}` : ""}` },

            ]);

            navigate("assistant");

          }}>

            {t.details} <Icon name="arrow" />

          </button>

          {url && (

            <a className="external-link" href={url} target="_blank" rel="noreferrer" aria-label={t.official}>

              <Icon name="external" />

            </a>

          )}

        </div>

      </article>

    );

  }



  function renderSchemeGrid(list) {

    if (!list.length) {

      return (

        <div className="empty-state">

          <div className="empty-icon"><Icon name="search" /></div>

          <h3>{t.noSchemes}</h3>

          <p>{t.noSchemesText}</p>

        </div>

      );

    }

    return <div className="scheme-grid">{list.map((scheme, index) => renderSchemeCard(scheme, index))}</div>;

  }



  function renderDashboard() {

    return (

      <>

        <section className="hero-panel">

          <div className="hero-copy">

            <div className="eyebrow"><span className="eyebrow-dot" /> {t.help}</div>

            <h1>{t.heroTitle}<br /><span>{t.heroAccent}</span></h1>

            <p>{t.heroText}</p>

            <div className="hero-actions">

              <button className="button button-dark" onClick={() => navigate("schemes")}>

                {t.explore} <Icon name="arrow" />

              </button>

              <button className="button button-light" onClick={startEligibility}>

                <Icon name="sparkle" /> {t.check}

              </button>

            </div>

            <div className="hero-trust">

              <span className="trust-icon"><Icon name="shield" /></span>

              <span>{t.verifiedNote}</span>

            </div>

          </div>

          <div className="hero-art" aria-hidden="true">

            <div className="art-orbit orbit-one" />

            <div className="art-orbit orbit-two" />

            <div className="art-sun" />

            <div className="art-leaf leaf-one" />

            <div className="art-leaf leaf-two" />

            <div className="art-leaf leaf-three" />

            <div className="art-card art-card-top">

              <span className="art-card-icon"><Icon name="check" /></span>

              <div><strong>One clear path</strong><small>Guidance for every step</small></div>

            </div>

            <div className="art-card art-card-bottom">

              <span className="mini-avatar">CS</span>

              <div><strong>Your public services guide</strong><small>Here to help you navigate</small></div>

              <span className="online-dot" />

            </div>

            <div className="art-center-mark"><Icon name="leaf" /></div>

          </div>

        </section>



        <section className="quick-actions-section">

          <div className="section-heading">

            <div><span className="section-kicker">GET STARTED</span><h2>{t.popular}</h2></div>

          </div>

          <div className="quick-action-grid">

            {[

              { title: t.learning, icon: "✳", cat: "Education", desc: "Scholarships & student support" },

              { title: t.farming, icon: "♧", cat: "Agriculture", desc: "Agriculture-related programmes" },

              { title: t.health, icon: "✚", cat: "Healthcare", desc: "Health support & services" },

              { title: t.work, icon: "↗", cat: "Employment", desc: "Jobs, training & skills" },

            ].map((item, i) => (

              <button

                className={`quick-action quick-action-${i}`}

                key={item.cat}

                onClick={() => { setCategory(item.cat); navigate("schemes"); }}

              >

                <span className="quick-action-icon">{item.icon}</span>

                <span className="quick-action-text"><strong>{item.title}</strong><small>{item.desc}</small></span>

                <span className="quick-action-arrow"><Icon name="arrow" /></span>

              </button>

            ))}

          </div>

        </section>



        <section className="featured-section">

          <div className="section-heading">

            <div><span className="section-kicker">A LITTLE HELP GOES A LONG WAY</span><h2>{t.recommended}</h2></div>

            <button className="view-all" onClick={() => navigate("schemes")}>{t.seeAll} <Icon name="arrow" /></button>

          </div>

          {renderSchemeGrid(schemes.slice(0, 3))}

        </section>



        <section className="eligibility-banner">

          <div className="banner-icon"><Icon name="sparkle" /></div>

          <div className="banner-copy"><h2>{t.featured}</h2><p>{t.featuredText}</p></div>

          <button className="button button-cream" onClick={startEligibility}>{t.start} <Icon name="arrow" /></button>

          <div className="banner-decoration" aria-hidden="true">✳</div>

        </section>

      </>

    );

  }



  function renderSchemesPage() {

    const categories = t.categories;

    return (

      <section className="page-section">

        <div className="page-intro">

          <span className="section-kicker">YOUR OPPORTUNITIES</span>

          <h1>{t.allSchemes}</h1>

          <p>{t.allSchemesText}</p>

        </div>

        <div className="scheme-toolbar">

          <label className="search-box">

            <Icon name="search" />

            <input value={search} onChange={(event) => setSearch(event.target.value)} placeholder={t.search} />

            {search && <button onClick={() => setSearch("")} aria-label="Clear search"><Icon name="close" /></button>}

          </label>

          <div className="category-tabs">

            {categories.map((label) => (

              <button

                key={label}

                className={category === categoryValue(label) ? "active" : ""}

                onClick={() => setCategory(categoryValue(label))}

              >{label}</button>

            ))}

          </div>

        </div>

        <div className="results-meta"><span>{filteredSchemes.length} {lang === "te" ? "పథకాలు" : "schemes listed"}</span><span>{apiOnline ? t.online : t.offline}</span></div>

        {renderSchemeGrid(filteredSchemes)}

      </section>

    );

  }



  function renderSavedPage() {

    return (

      <section className="page-section">

        <div className="page-intro">

          <span className="section-kicker">YOUR PERSONAL LIST</span>

          <h1>{t.savedTitle}</h1>

          <p>{t.savedText}</p>

        </div>

        {savedSchemes.length ? renderSchemeGrid(savedSchemes) : (

          <div className="empty-state empty-state-large">

            <div className="empty-icon"><Icon name="bookmark" /></div>

            <h3>{t.emptySaved}</h3>

            <p>{t.emptySavedText}</p>

            <button className="button button-dark" onClick={() => navigate("schemes")}>{t.explore} <Icon name="arrow" /></button>

          </div>

        )}

      </section>

    );

  }



  function renderChatPage() {

    return (

      <section className="chat-page">

        <div className="chat-intro">

          <div className="chat-avatar-large"><Icon name="sparkle" /></div>

          <span className="section-kicker">CIVICSAATHI GUIDE</span>

          <h1>{t.chatTitle}</h1>

          <p>{t.chatSub}</p>

        </div>

        <div className="chat-shell">

          <div className="chat-shell-header">

            <div className="chat-agent-avatar"><Icon name="leaf" /></div>

            <div className="chat-agent-details"><strong>{t.brand} Guide</strong><span><i /> {lang === "te" ? "మార్గదర్శక సహాయకుడు" : "Guided assistance"}</span></div>

            <span className="chat-header-badge"><Icon name="shield" /> {lang === "te" ? "సురక్షిత మార్గదర్శకం" : "Guided support"}</span>

          </div>

          <div className="chat-messages">

            {messages.map((message, index) => (

              <div className={`message-row ${message.role === "user" ? "message-user" : ""}`} key={`${index}-${message.role}`}>

                {message.role === "assistant" && <div className="message-avatar"><Icon name="leaf" /></div>}

                <div className={`message-bubble ${message.role === "user" ? "user-bubble" : ""}`}>

                  <p>{message.text}</p>

                  {message.action === "eligibility" && (

                    <button className="button button-dark message-action" onClick={startEligibility}>{t.check} <Icon name="arrow" /></button>

                  )}

                  {message.action === "schemes" && (

                    <button className="inline-action" onClick={() => navigate("schemes")}>{t.explore} <Icon name="arrow" /></button>

                  )}

                  {message.suggestions && (

                    <div className="message-suggestions">

                      {t.prompts.map((prompt) => <button key={prompt} onClick={() => sendChat(prompt)}>{prompt}</button>)}

                    </div>

                  )}

                </div>

              </div>

            ))}

            {chatLoading && (
              <div className="message-row">
                <div className="message-avatar"><Icon name="leaf" /></div>
                <div className="message-bubble">
                  <p>{lang === "te" ? "సమాధానం సిద్ధం చేస్తోంది..." : "CivicSaathi is thinking..."}</p>
                  <span className="spinner" />
                </div>
              </div>
            )}
          </div>

          <div className="chat-suggestions">

            <span>{t.tryAsking}</span>

            {t.prompts.map((prompt) => <button key={prompt} onClick={() => sendChat(prompt)}>{prompt}</button>)}

          </div>

          <form className="chat-composer" onSubmit={(event) => { event.preventDefault(); sendChat(); }}>

            <input value={chatInput} onChange={(event) => setChatInput(event.target.value)} placeholder={t.chatPlaceholder} aria-label={t.chatPlaceholder} />

            <button type="submit" disabled={!chatInput.trim() || chatLoading} aria-label={t.send}><Icon name="send" /></button>

          </form>

          <p className="chat-disclaimer">{t.disclaimer}</p>

        </div>

      </section>

    );

  }



  function renderWizard() {

    if (!wizardOpen) return null;

    const answeredCount = Object.keys(answers).length;

    const totalQuestions = 12;

    const progress = Math.min(100, Math.round((answeredCount / totalQuestions) * 100));

    const questionText = question

      ? QUESTION_LABELS[question.field]?.[lang] ?? question.question

      : "";



    const translateOption = (option) => lang === "te" ? OPTION_TRANSLATIONS[option] ?? option : option;

    const resultStatus = result?.status;

    const statusLabel = resultStatus === "POTENTIAL_MATCH"

      ? t.potential

      : resultStatus === "NOT_MATCHED"

        ? t.notMatched

        : t.needsInfo;



    return (

      <div className="wizard-backdrop" role="presentation" onMouseDown={(event) => {

        if (event.target === event.currentTarget) closeWizard();

      }}>

        <section className="wizard-modal" role="dialog" aria-modal="true" aria-label={t.checkEligibility}>

          <header className="wizard-header">

            <div className="wizard-brand"><span className="wizard-logo"><Icon name="leaf" /></span><span>{t.brand}<small>{t.step}</small></span></div>

            <button className="icon-button" onClick={closeWizard} aria-label={t.close}><Icon name="close" /></button>

          </header>



          {!result && !question && !wizardLoading && (

            <div className="wizard-start">

              <div className="wizard-illustration"><Icon name="sparkle" /></div>

              <span className="section-kicker">PERSONALIZED GUIDANCE</span>

              <h2>{t.profileTitle}</h2>

              <p>{t.profileText}</p>

              <label className="field-label">{t.state}

                <select value={profile.state} onChange={(event) => setProfile({ ...profile, state: event.target.value })}>

                  <option value="Andhra Pradesh">Andhra Pradesh</option>

                  <option value="Telangana">Telangana</option>

                  <option value="Other">Other state</option>

                </select>

              </label>

              <label className="field-label">{t.education}

                <select value={profile.educationLevel} onChange={(event) => setProfile({ ...profile, educationLevel: event.target.value })}>

                  <option value="School">School</option>

                  <option value="Intermediate">Intermediate</option>

                  <option value="Undergraduate">Undergraduate / Degree</option>

                  <option value="Postgraduate">Postgraduate</option>

                  <option value="Other">Other</option>

                </select>

              </label>

              <button className="button button-dark button-full" onClick={beginQuestions}>{t.start} <Icon name="arrow" /></button>

            </div>

          )}



          {wizardLoading && (

            <div className="wizard-loading"><span className="spinner" /><h3>{t.loading}</h3><p>{t.assistantThinking}</p></div>

          )}



          {question && !result && !wizardLoading && (

            <div className="wizard-question">

              <div className="progress-meta"><span>{t.questionOf} {Math.min(answeredCount + 1, totalQuestions)} {t.of} {totalQuestions}</span><span>{progress}%</span></div>

              <div className="progress-track"><div style={{ width: `${progress}%` }} /></div>

              <div className="question-step">QUESTION {String(Math.min(answeredCount + 1, totalQuestions)).padStart(2, "0")}</div>

              <h2>{questionText}</h2>

              <p className="question-hint">{question.inputType === "select" ? t.chooseOption : t.enterNumber}</p>

              {question.inputType === "select" ? (

                <div className="answer-options">

                  {(question.options ?? []).map((option) => (

                    <button

                      key={option}

                      className={`answer-option ${draft === option ? "selected" : ""}`}

                      onClick={() => { setDraft(option); setError(""); }}

                    >

                      <span>{translateOption(option)}</span><span className="option-radio">{draft === option && <i />}</span>

                    </button>

                  ))}

                </div>

              ) : (

                <input

                  className="number-answer"

                  type="number"

                  min="0"

                  step="any"

                  value={draft}

                  onChange={(event) => { setDraft(event.target.value); setError(""); }}

                  placeholder={t.enterNumber}

                />

              )}

              {error && <div className="inline-error">{error}</div>}

              <div className="wizard-actions">

                <button className="button button-light" onClick={goBackQuestion} disabled={answeredCount === 0}><Icon name="back" /> {t.back}</button>

                <button className="button button-dark" onClick={continueQuestion}>{t.continue} <Icon name="arrow" /></button>

              </div>

            </div>

          )}



          {result && (

            <div className="wizard-result">

              <div className={`result-mark ${resultStatus === "POTENTIAL_MATCH" ? "result-positive" : ""}`}><Icon name={resultStatus === "POTENTIAL_MATCH" ? "check" : "sparkle"} /></div>

              <span className="section-kicker">YOUR PRELIMINARY OVERVIEW</span>

              <h2>{t.resultTitle}</h2>

              <div className="result-status"><span>{t.status}</span><strong>{statusLabel}</strong></div>

              {result.schemeName && <h3 className="result-scheme-name">{result.schemeName}</h3>}

              {result.message && <p className="result-message">{result.message}</p>}

              {result.matchedConditions?.length > 0 && (

                <div className="result-list"><h4><Icon name="check" /> {t.matched}</h4><ul>{result.matchedConditions.map((item, i) => <li key={i}>{item}</li>)}</ul></div>

              )}

              {result.unmetConditions?.length > 0 && (

                <div className="result-list result-list-warning"><h4><Icon name="sparkle" /> {t.unmet}</h4><ul>{result.unmetConditions.map((item, i) => <li key={i}>{item}</li>)}</ul></div>

              )}

              {result.missingInformation?.length > 0 && (

                <div className="result-list"><h4>{t.missing}</h4><ul>{result.missingInformation.map((item, i) => <li key={i}>{item}</li>)}</ul></div>

              )}

              {result.officialWebsite && /^https?:\/\//i.test(result.officialWebsite) && (

                <a className="official-result-link" href={result.officialWebsite} target="_blank" rel="noreferrer">{t.official} <Icon name="external" /></a>

              )}

              <p className="result-disclaimer">{t.disclaimer}</p>

              <div className="wizard-actions">

                <button className="button button-light" onClick={closeWizard}>{t.close}</button>

                <button className="button button-dark" onClick={startEligibility}>{t.restart} <Icon name="arrow" /></button>

              </div>

            </div>

          )}



          {error && !question && !result && !wizardLoading && (

            <div className="wizard-error"><p>{error}</p><button className="button button-dark" onClick={retryConnection}>{t.retry}</button></div>

          )}

        </section>

      </div>

    );

  }



  const navItems = [

    { id: "dashboard", label: t.dashboard, icon: "home" },

    { id: "schemes", label: t.schemes, icon: "search" },

    { id: "assistant", label: t.assistant, icon: "chat" },

    { id: "saved", label: t.saved, icon: "bookmark", count: savedIds.length },

  ];
  async function handleLogout() {
    try {
      const csrfHeaders = await getCsrfHeaders();
      const response = await fetch(`${API}/auth/logout`, {
        method: "POST",
        credentials: "include",
        headers: csrfHeaders,
      });

      if (!response.ok) {
        throw new Error("Logout failed. Please try again.");
      }

      setUserEmail("");
    } catch (error) {
      window.alert(error.message || "Unable to log out. Please try again.");
    }
  }


  return (

    <div className="app-shell" data-text-size={textSize} style={{ "--user-text-color": textColor }}>

      <aside className="sidebar">

        <button className="brand-lockup" onClick={() => navigate("dashboard")}>

          <span className="brand-mark"><Icon name="leaf" /></span>

          <span className="brand-name">{t.brand}<small>PUBLIC SERVICE COMPANION</small></span>

        </button>



        <div className="sidebar-label">{lang === "te" ? "మీ కార్యస్థలం" : "WORKSPACE"}</div>

        <nav className="side-nav">

          {navItems.map((item) => (

            <button key={item.id} className={`nav-item ${page === item.id ? "active" : ""}`} onClick={() => navigate(item.id)}>

              <span className="nav-icon"><Icon name={item.icon} /></span>

              <span>{item.label}</span>

              {item.count > 0 && <span className="nav-count">{item.count}</span>}

              {page === item.id && <span className="nav-active-mark" />}

            </button>

          ))}

        </nav>



        <div className="sidebar-help-card">

          <div className="help-sparkle"><Icon name="sparkle" /></div>

          <strong>{lang === "te" ? "ఎక్కడ ప్రారంభించాలో తెలియదా?" : "Not sure where to start?"}</strong>

          <p>{lang === "te" ? "మీకు సరిపోయే పథకాలను కనుగొనండి." : "Let us help you find relevant schemes."}</p>

          <button onClick={startEligibility}>{t.check} <Icon name="arrow" /></button>

          <div className="help-card-decoration">✳</div>

        </div>



        <div className="sidebar-bottom">

          <div className={`service-status ${apiOnline ? "connected" : ""}`}><span />{apiOnline ? t.online : t.offline}</div>

          <div className="sidebar-footer"><span className="footer-mini-mark">CS</span><span>{t.brand}<small>Designed for everyone</small></span></div>

        </div>

      </aside>



      <main className="main-area">

        <header className="topbar">

          <div className="breadcrumb"><span>{t.brand}</span><Icon name="arrow" /><strong>{navItems.find((item) => item.id === page)?.label ?? t.dashboard}</strong></div>

          <div className="topbar-actions">

            <span className={`topbar-status ${apiOnline ? "connected" : ""}`}><i />{apiOnline ? t.online : t.offline}</span>

            <div className="language-switch" aria-label="Choose language">

              <button className={lang === "en" ? "selected" : ""} onClick={() => setLang("en")}>EN</button>

              <span />

              <button className={lang === "te" ? "selected" : ""} onClick={() => setLang("te")}>తె</button>

            </div>

            <button
              className="settings-trigger"
              onClick={() => setSettingsOpen(true)}
              title="Appearance settings"
              aria-label="Open appearance settings"
            >
              <span aria-hidden="true">Aa</span>
              <span>Settings</span>
            </button>
            <button className="profile-button" onClick={startEligibility} title={t.check}><span>CS</span></button>
            <button
              type="button"
              className="settings-trigger"
              onClick={handleLogout}
              title="Log out"
              aria-label="Log out of CivicSaathi"
            >
              <span>Logout</span>
            </button>

          </div>

        </header>



        <div className="content-area">

          {page === "dashboard" && renderDashboard()}

          {page === "schemes" && renderSchemesPage()}

          {page === "assistant" && renderChatPage()}

          {page === "saved" && renderSavedPage()}

          <footer className="main-footer"><span>© {new Date().getFullYear()} {t.brand}</span><span><Icon name="shield" /> {t.footer}</span></footer>

        </div>

      </main>



      {settingsOpen && (
        <div
          className="appearance-overlay"
          onMouseDown={(event) => {
            if (event.target === event.currentTarget) {
              setSettingsOpen(false);
            }
          }}
        >
          <section
            className="appearance-panel"
            role="dialog"
            aria-modal="true"
            aria-labelledby="appearance-title"
          >
            <div className="appearance-heading">
              <div>
                <span className="appearance-eyebrow">PERSONALIZE</span>
                <h2 id="appearance-title">Appearance settings</h2>
                <p>Make CivicSaathi comfortable for you to read.</p>
              </div>
              <button
                type="button"
                className="appearance-close"
                onClick={() => setSettingsOpen(false)}
                aria-label="Close settings"
              >
                ×
              </button>
            </div>

            <div className="appearance-field">
              <label htmlFor="civic-text-size">Text size</label>
              <select
                id="civic-text-size"
                value={textSize}
                onChange={(event) => setTextSize(event.target.value)}
              >
                <option value="small">Small</option>
                <option value="medium">Medium</option>
                <option value="large">Large</option>
              </select>
              <p>Choose a size that feels comfortable to read.</p>
            </div>

            <div className="appearance-field">
              <label htmlFor="civic-text-color">Text color</label>
              <div className="appearance-color-row">
                <input
                  id="civic-text-color"
                  type="color"
                  value={textColor}
                  onChange={(event) => setTextColor(event.target.value)}
                />
                <span>{textColor.toUpperCase()}</span>
              </div>
              <p>Your selected color will be applied to the main text.</p>
            </div>

            <div className="appearance-preview">
              <strong>Preview</strong>
              <p style={{ color: textColor, fontSize: textSize === "small" ? "14px" : textSize === "large" ? "20px" : "16px" }}>
                Government services made easier to understand.
              </p>
            </div>

            <div className="appearance-actions">
              <button
                type="button"
                className="appearance-reset"
                onClick={() => {
                  setTextSize("medium");
                  setTextColor("#203b31");
                }}
              >
                Reset to default
              </button>
              <button
                type="button"
                className="appearance-done"
                onClick={() => setSettingsOpen(false)}
              >
                Done
              </button>
            </div>
          </section>
        </div>
      )}

      {renderWizard()}

    </div>

  );

}



export default App;