package com.callamechanic.status;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping(value = {"/", "/api/v1"}, produces = MediaType.TEXT_HTML_VALUE)
    public String root() {
        return """
                <!doctype html>
                <html lang="en">
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>Call-A-Mechanic API</title>
                    <style>
                        :root {
                            color-scheme: light dark;
                            font-family: Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
                            background: #edf5ff;
                            color: #0f172a;
                        }
                        body {
                            min-height: 100vh;
                            margin: 0;
                            display: grid;
                            place-items: center;
                            background:
                                linear-gradient(135deg, rgba(47, 107, 255, 0.16), rgba(255, 255, 255, 0.92)),
                                radial-gradient(circle at 20% 80%, rgba(47, 107, 255, 0.16), transparent 32rem);
                        }
                        main {
                            width: min(92vw, 520px);
                            padding: 34px;
                            border: 1px solid #d9e2f2;
                            border-radius: 24px;
                            background: rgba(255, 255, 255, 0.84);
                            box-shadow: 0 24px 70px rgba(15, 23, 42, 0.14);
                            backdrop-filter: blur(12px);
                        }
                        .logo {
                            width: 54px;
                            height: 54px;
                            border-radius: 16px;
                            display: grid;
                            place-items: center;
                            background: #2f6bff;
                            color: white;
                            font-size: 16px;
                            font-weight: 900;
                            letter-spacing: 0;
                            margin-bottom: 22px;
                        }
                        .status {
                            display: inline-flex;
                            align-items: center;
                            gap: 8px;
                            padding: 8px 12px;
                            border-radius: 999px;
                            background: #dcfce7;
                            color: #166534;
                            font-weight: 700;
                            font-size: 13px;
                        }
                        .dot {
                            width: 8px;
                            height: 8px;
                            border-radius: 999px;
                            background: #16a34a;
                            box-shadow: 0 0 0 5px rgba(22, 163, 74, 0.14);
                        }
                        h1 {
                            margin: 18px 0 8px;
                            font-size: clamp(28px, 5vw, 38px);
                            line-height: 1.05;
                        }
                        p {
                            margin: 0;
                            color: #64748b;
                            line-height: 1.6;
                        }
                        code {
                            display: inline-block;
                            margin-top: 18px;
                            padding: 10px 12px;
                            border-radius: 12px;
                            background: #eef4ff;
                            color: #1e5bff;
                            font-weight: 700;
                        }
                        @media (prefers-color-scheme: dark) {
                            :root {
                                background: #07111f;
                                color: #eaf2ff;
                            }
                            body {
                                background:
                                    linear-gradient(135deg, rgba(47, 107, 255, 0.34), rgba(7, 17, 31, 0.95)),
                                    radial-gradient(circle at 20% 80%, rgba(96, 165, 250, 0.18), transparent 32rem);
                            }
                            main {
                                border-color: #2a4367;
                                background: rgba(15, 31, 54, 0.86);
                                box-shadow: 0 24px 70px rgba(0, 0, 0, 0.34);
                            }
                            p { color: #9fb4d2; }
                            code {
                                background: #172d4a;
                                color: #93c5fd;
                            }
                        }
                    </style>
                </head>
                <body>
                    <main>
                        <div class="logo" aria-hidden="true">CAM</div>
                        <span class="status"><span class="dot"></span> Online</span>
                        <h1>Call-A-Mechanic API</h1>
                        <p>The backend service is running and ready to receive authenticated requests from the web and Android applications.</p>
                        <code>/api/v1</code>
                    </main>
                </body>
                </html>
                """;
    }

    @GetMapping("/healthz")
    public Map<String, Object> health() {
        return Map.of(
                "service", "Call-A-Mechanic API",
                "status", "online",
                "timestamp", Instant.now().toString()
        );
    }
}
