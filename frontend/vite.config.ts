import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  test: {
    setupFiles: './tests/setup.ts'
  },
  server: {
    port: 5173
  }
})
