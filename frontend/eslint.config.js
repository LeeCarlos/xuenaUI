import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'

export default [
  { ignores: ['dist', 'coverage'] },
  {
    files: ['**/*.{js,jsx}'],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
      parserOptions: {
        ecmaVersion: 'latest',
        ecmaFeatures: { jsx: true },
        sourceType: 'module',
      },
    },
    plugins: {
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
    },
    rules: {
      ...js.configs.recommended.rules,
      ...reactHooks.configs.recommended.rules,

      'no-unused-vars': ['error', { varsIgnorePattern: '^[A-Z_]' }],
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],

      'no-console': ['warn', { allow: ['warn', 'error'] }],
      'no-debugger': 'error',
      'no-alert': 'warn',
      'prefer-const': 'error',
      'no-var': 'error',
      'eqeqeq': ['error', 'always'],
      'no-eval': 'error',
      'no-implied-eval': 'error',
      'no-throw-literal': 'error',
      'no-unused-expressions': 'error',
      'no-useless-concat': 'error',
      'prefer-template': 'warn',
      'no-duplicate-imports': 'error',
      'no-use-before-define': ['error', { functions: false, classes: true, variables: true }],
      'no-async-promise-executor': 'error',
      'no-empty': ['error', { allowEmptyCatch: true }],
      'no-extra-semi': 'error',
      'no-constant-condition': ['warn', { checkLoops: false }],
      'no-shadow': 'warn',
      'no-redeclare': 'error',
      'no-useless-rename': 'error',
      'object-shorthand': 'warn',
      'prefer-spread': 'warn',
      'prefer-rest-params': 'warn',
      'arrow-body-style': ['warn', 'as-needed'],
      'prefer-arrow-callback': 'warn',
    },
  },
  {
    files: ['src/services/**/*.{js,jsx}'],
    rules: {
      'no-console': 'off',
    },
  },
]
