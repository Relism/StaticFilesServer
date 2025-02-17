import './App.css';

import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import FileExplorer from './components/FileExplorer';
import Error404 from './pages/Error404';
import Error403 from './pages/Error403';

function App() {
  return (
      <Router>
        <Routes>
          <Route path="/*" element={<FileExplorer />} />
          <Route path="/403" element={<Error403 />} />
          <Route path="/404" element={<Error404 />} />
        </Routes>
      </Router>
  );
}

export default App;
