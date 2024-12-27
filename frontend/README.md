# Introduction

The project was created with [React Router v7](https://reactrouter.com/start/framework/installation) and [Vite](https://vite.dev/) (TypeScript).

**Note:** Vite has HMR (Hot Module Replacement) meaning that the web page is automatically updated while writing code.

The following dependencies are already added to [package.json](package.json) and [package-lock.json](package-lock.json):
- React Router v7 (routing as framework)
- NextUI v2.6.10 (component library)
- Tailwind CSS v3.4.16 (CSS framework)


## Setup

Make sure you have:
- node.js v22.12.0
- npm v10.9.0

Install the dependencies (inside `frontend` directory):
```bash
npm install
```


## Running

Start the development server from:

- **terminal**

    ```bash
    npm run dev
    ```

- **WebStorm**

    ```
    1. Edit configurations (at the top-right corner)
    2. Add a new NPM configuration
    3. Give it a name
    4. Select the package.json (it should be already detected)
    5. Select run as the command
    6. Select dev as the script
    7. Apply & close
    8. Click the run button (at the top-right corner)
    ```

The web application will be available at http://localhost:5173.


## Conventions

If you want to be cool 😎, then follow these conventions:
- `PascalCase`: class / interface / type / enum
- `lowerCamelCase`: variable / parameter / function / method / propery / module alias
- `CONSTANT_CASE`: global constant values / enum values
- indent: 4 whitespaces (**no tabs**)

Tips:
- if you are using **Visual Studio Code**, enable `Use Tab Stops` in settings


## Useful links

- React Router v7: https://reactrouter.com/home
- NextUI: https://nextui.org/docs/guide/introduction
- Tailwind CSS: https://tailwindcss.com/