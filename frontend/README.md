Welcome to your new TanStack app!

# Getting Started

To run this application:

```bash
npm install
npm run start  
```

# Building For Production

To build this application for production:

```bash
npm run build
```

## Testing

This project uses [Vitest](https://vitest.dev/) for testing. You can run the tests with:

```bash
npm run test
```

## Styling

This project uses [Tailwind CSS](https://tailwindcss.com/) for styling.

## Linting & Formatting

This project uses [eslint](https://eslint.org/) and [prettier](https://prettier.io/) for linting and formatting. Eslint
is configured using [tanstack/eslint-config](https://tanstack.com/config/latest/docs/eslint). The following scripts are
available:

```bash
npm run lint
npm run format
npm run check
```

## Routing

This project uses [TanStack Router](https://tanstack.com/router). The initial setup is a code based router. Which means
that the routes are defined in code (in the `./src/main.tsx` file). If you like you can also use a file based routing
setup by following
the [File Based Routing](https://tanstack.com/router/latest/docs/framework/react/guide/file-based-routing) guide.


## Data Fetching

There are multiple ways to fetch data in your application. You can use TanStack Query to fetch data from a server. But
you can also use the `loader` functionality built into TanStack Router to load the data for a route before it's
rendered.

For example:

```tsx
const peopleRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/people",
  loader: async () => {
    const response = await fetch("https://swapi.dev/api/people");
    return response.json() as Promise<{
      results: {
        name: string;
      }[];
    }>;
  },
  component: () => {
    const data = peopleRoute.useLoaderData();
    return (
      <ul>
        {data.results.map((person) => (
          <li key={person.name}>{person.name}</li>
        ))}
      </ul>
    );
  },
});
```

Loaders simplify your data fetching logic dramatically. Check out more information in
the [Loader documentation](https://tanstack.com/router/latest/docs/framework/react/guide/data-loading#loader-parameters).

### React-Query

React-Query is an excellent addition or alternative to route loading and integrating it into you application is a
breeze.

First add your dependencies:



# Learn More

You can learn more about all of the offerings from TanStack in the [TanStack documentation](https://tanstack.com).
