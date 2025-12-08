import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import BookCreatePage from './components/BookCreatePage';
import BookEditPage from './components/BookEditPage';
import InfoPage from './components/InfoPage';

const theme = createTheme({
    palette: {
        mode: 'light', // 필요에 따라 'dark' 가능
    },
    secondary: {
      main: '#dc004e',
    },
  },
  typography: {
    fontFamily: [
      '-apple-system',
      'BlinkMacSystemFont',
      '"Segoe UI"',
      'Roboto',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
    ].join(','),
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/books/create" replace />} />
          <Route path="/books/create" element={<BookCreatePage />} />
          <Route path="/books/:id/edit" element={<BookEditPage />} />
          <Route path="/books/:id" element={<InfoPage />} />
        </Routes>
      </BrowserRouter>
    </ThemeProvider>
  );
}

export default App;
