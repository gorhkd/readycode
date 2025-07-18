import { type ReactNode, useState } from 'react'
import { MutationCache, QueryCache, QueryClient, QueryClientProvider } from '@tanstack/react-query'

export default function QueryProvider({ children }: { children: ReactNode }) {
  const [queryClient] = useState(
    () =>
      new QueryClient({
        defaultOptions: {
          queries: {
            refetchOnWindowFocus: false,
            staleTime: 1000 * 60 * 10,
          },
        },
        queryCache: new QueryCache({
          onError: (error: Error) => handleError('쿼리 에러 발생: ', error),
        }),
        mutationCache: new MutationCache({
          onError: (error: Error) => handleError('뮤테이션 에러 발생: ', error),
        }),
      }),
  )

  return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
}

const handleError = (errorType: string, error: unknown) => {
  console.error(errorType, '\n', error)
}
