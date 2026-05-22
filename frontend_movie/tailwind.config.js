/** @type {import('tailwindcss').Config} */

export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'menu-bg': 'rgba(10, 10, 10, 0.4)', // The semi-transparent dark overlay for the entire menu bar
        'search-fill': 'rgba(255, 255, 255, 0.1)', // A lighter translucent fill for inside the search input
        'glass-border': 'rgba(255, 255, 255, 0.35)',
        'glass-fill': 'rgba(255, 255, 255, 0.12)',
      },
      
    },
  },
  plugins: [],
}