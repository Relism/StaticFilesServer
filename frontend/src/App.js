import './App.css';
import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import FileExplorer from './components/FileExplorer';
import Error404 from './pages/Error404';
import Error403 from './pages/Error403';

function App() {
    const [config, setConfig] = useState({ title: '', logoUrl: '', faviconUrl: '' });

    useEffect(() => {
        const fetchConfig = async () => {
            try {
                const response = await fetch('/api/configuration');
                const data = await response.json();
                setConfig(data);
                document.title = data.title;

                const link = document.createElement('link');
                link.rel = 'icon';
                link.href = data.faviconUrl;
                document.head.appendChild(link);
            } catch (err) {
                console.error('Failed to fetch configuration:', err);
            }
        };

        fetchConfig();
    }, []);

    return (
        <Router>
            <Routes>
                <Route path="/*" element={<FileExplorer config={config} />} />
                <Route path="/403" element={<Error403 logoUrl={config.logoUrl} />} />
                <Route path="/404" element={<Error404 logoUrl={config.logoUrl} />} />
            </Routes>
        </Router>
    );
}

export default App;