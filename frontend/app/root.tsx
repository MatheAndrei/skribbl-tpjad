import {
    isRouteErrorResponse,
    Links,
    Meta,
    Outlet,
    Scripts,
    ScrollRestoration,
    useHref,
    useNavigate,
} from "react-router";
import type { Route } from "./+types/root";
import type {NavigateOptions} from "react-router";
import {NextUIProvider} from "@nextui-org/react";
import stylesheet from "./app.css?url";
import {configure} from "mobx";


// linting options for MobX
configure({
    computedRequiresReaction: true,
    reactionRequiresObservable: true,
});


// for NextUI components to have autocomplete and type safety when using the router
declare module "@react-types/shared" {
    interface RouterConfig {
        routerOptions: NavigateOptions;
    }
}


export const links: Route.LinksFunction = () => [
    { rel: "stylesheet", href: stylesheet },
];


export function Layout({ children }: { children: React.ReactNode }) {
    return (
        <html lang="en">
            <head>
                <meta charSet="utf-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <Meta />
                <Links />
            </head>
            <body>
                {children}
                <ScrollRestoration />
                <Scripts />
            </body>
        </html>
    );
}


export default function App() {
    const navigate = useNavigate();

    return (
        <NextUIProvider navigate={navigate} useHref={useHref}>
            <Outlet/>
        </NextUIProvider>
    );
}


export function ErrorBoundary({ error }: Route.ErrorBoundaryProps) {
    let message = "Oops!";
    let details = "An unexpected error occurred.";
    let stack: string | undefined;

    if (isRouteErrorResponse(error)) {
        message = error.status === 404 ? "404" : "Error";
        details =
            error.status === 404
                ? "The requested page could not be found."
                : error.statusText || details;
    } else if (import.meta.env.DEV && error && error instanceof Error) {
        details = error.message;
        stack = error.stack;
    }

    return (
        <main>
            <h1>{message}</h1>
            <p>{details}</p>
            {stack && (
                <pre>
                    <code>{stack}</code>
                </pre>
            )}
        </main>
    );
}
