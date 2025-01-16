import type { Config } from "tailwindcss";
import {nextui} from "@nextui-org/react";

export default {
    content: [
        "./app/**/{**,.client,.server}/**/*.{js,jsx,ts,tsx}",
        "./node_modules/@nextui-org/theme/dist/**/*.{js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {
            fontFamily: {
                sans: ["Pangolin", "ui-sans-serif", "system-ui", "sans-serif", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji"],
            },
            keyframes: {
                wiggle: {
                    "0%, 100%": { transform: "rotate(-4deg)" },
                    "50%": { transform: "rotate(4deg)" },
                }
            }
        },
    },
    darkMode: "class",
    plugins: [nextui()],
} satisfies Config;
