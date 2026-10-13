import { useState } from "react";
import "./AuthPage.css";

export default function AuthPage({ onAuthenticated }) {
  const [mode, setMode] = useState(() => new URLSearchParams(window.location.search).has("token") ? "verify" : "login");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  async function submit(event) {
    event.preventDefault();
    setMessage("");
    setLoading(true);

    try {
      if (mode === "verify") {
        const token = new URLSearchParams(window.location.search).get("token");
        if (!token) throw new Error("Verification token is missing.");

        const response = await fetch(
          `http://localhost:8088/api/auth/verify?token=${encodeURIComponent(token)}`,
          { credentials: "include" }
        );
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || "Verification failed.");

        setMessage(data.message || "Email verified. You can now log in.");
        setMode("login");
        window.history.replaceState({}, "", window.location.pathname);
        return;
      }

      const csrfResponse = await fetch(
        "http://localhost:8088/api/auth/csrf",
        { credentials: "include" }
      );

      await csrfResponse.json().catch(() => ({}));

      const csrfCookie = document.cookie
        .split("; ")
        .find((item) => item.startsWith("XSRF-TOKEN="));

      const csrfToken = csrfCookie
        ? decodeURIComponent(csrfCookie.substring("XSRF-TOKEN=".length))
        : "";

      if (!csrfResponse.ok || !csrfToken) {
        throw new Error("Could not start a secure session. Please try again.");
      }

      const endpoint = mode === "register" ? "register" : "login";
      const response = await fetch(
        `http://localhost:8088/api/auth/${endpoint}`,
        {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            "X-XSRF-TOKEN": csrfToken
          },
          body: JSON.stringify({ email, password })
        }
      );

      const data = await response.json().catch(() => ({}));
      if (!response.ok) throw new Error(data.message || "Request failed.");

      setMessage(data.message || "Success.");

      if (mode === "login") {
        onAuthenticated?.(data.email || email);
      } else {
        setPassword("");
      }
    } catch (error) {
      setMessage(error.message || "Something went wrong.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="auth-page">
      <section className="auth-intro">
        <div className="auth-brand">
          <span className="auth-brand-mark">C</span>
          <span>CivicSaathi</span>
        </div>

        <div className="auth-intro-content">
          <span className="auth-eyebrow">YOUR PUBLIC SERVICE COMPANION</span>
          <h1>Government services, made simpler.</h1>
          <p>
            Explore schemes, understand eligibility, and find official
            information in one welcoming place.
          </p>
          <div className="auth-feature">
            <span>✓</span>
            <div>
              <strong>Find relevant schemes</strong>
              <p>Discover services that may fit your needs.</p>
            </div>
          </div>
          <div className="auth-feature">
            <span>✓</span>
            <div>
              <strong>Get clear guidance</strong>
              <p>Ask questions in simple language.</p>
            </div>
          </div>
          <div className="auth-feature">
            <span>✓</span>
            <div>
              <strong>Your account, protected</strong>
              <p>Email verification helps secure your account.</p>
            </div>
          </div>
        </div>
        <p className="auth-footer">Secure · Simple · Free</p>
      </section>

      <section className="auth-form-side">
        <div className="auth-card">
          <div className="auth-mobile-brand">CivicSaathi</div>

          {mode !== "verify" ? (
            <>
              <div className="auth-tabs">
                <button
                  type="button"
                  className={mode === "login" ? "active" : ""}
                  onClick={() => { setMode("login"); setMessage(""); }}
                >
                  Log in
                </button>
                <button
                  type="button"
                  className={mode === "register" ? "active" : ""}
                  onClick={() => { setMode("register"); setMessage(""); }}
                >
                  Create account
                </button>
              </div>

              <h2>{mode === "login" ? "Welcome back" : "Join CivicSaathi"}</h2>
              <p className="auth-subtitle">
                {mode === "login"
                  ? "Log in to continue to your account."
                  : "Create an account to get started."}
              </p>

              <form onSubmit={submit}>
                <label htmlFor="auth-email">Email address</label>
                <input
                  id="auth-email"
                  type="email"
                  autoComplete="email"
                  required
                  maxLength={254}
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                  placeholder="you@example.com"
                />

                <label htmlFor="auth-password">Password</label>
                <input
                  id="auth-password"
                  type="password"
                  autoComplete={mode === "login" ? "current-password" : "new-password"}
                  minLength={8}
                  maxLength={72}
                  required
                  value={password}
                  onChange={(event) => setPassword(event.target.value)}
                  placeholder="At least 8 characters"
                />

                <button className="auth-submit" type="submit" disabled={loading}>
                  {loading
                    ? "Please wait..."
                    : mode === "login" ? "Log in" : "Create account"}
                </button>
              </form>

              {mode === "register" && (
                <p className="auth-note">
                  We’ll send a verification link to your email before you can log in.
                </p>
              )}
            </>
          ) : (
            <>
              <h2>Verify your email</h2>
              <p className="auth-subtitle">
                Use the verification link sent to your email address.
              </p>
              <button
                type="button"
                className="auth-submit"
                disabled={loading}
                onClick={submit}
              >
                {loading ? "Verifying..." : "Verify email"}
              </button>
              <button
                type="button"
                className="auth-secondary"
                onClick={() => setMode("login")}
              >
                Back to login
              </button>
            </>
          )}

          {message && (
            <p className="auth-message" role="status" aria-live="polite">
              {message}
            </p>
          )}

          <p className="auth-legal">
            Use CivicSaathi to explore public services and scheme information.
          </p>
        </div>
      </section>
    </main>
  );
}