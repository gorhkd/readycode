//  @ts-check

import { tanstackConfig } from '@tanstack/eslint-config'

export default [
  { ignores: ['dist/**', 'node_modules/**', 'src/routeTree.gen.ts'] },
  ...tanstackConfig,
  {
    files: ['**/*.{ts,tsx,js,jsx}'],
    rules: {
      'sort-imports': 'warn',
      'import/order': 'warn',
      'import/consistent-type-specifier-style': 'warn',
    },
  },
]
