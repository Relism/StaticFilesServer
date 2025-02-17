import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import FileItem from './FileItem';
import Breadcrumb from './Breadcrumb';

const FileExplorer = ({ config }) => {
    const { '*': path } = useParams(); // Capture the wildcard path
    const [files, setFiles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchFiles = async () => {
            try {
                const response = await fetch(`/api/files/${path || ''}`);
                if (!response.ok) {
                    if (response.status === 403) navigate('/403');
                    if (response.status === 404) navigate('/404');
                    return;
                }
                const data = await response.json();
                setFiles(data.files || data);
                setLoading(false);
            } catch (err) {
                setError(err.message);
                setLoading(false);
            }
        };

        fetchFiles().then(r => r);
    }, [path, navigate]);

    if (loading) return <div className="spinner"></div>;
    if (error) return <p className="text-red-500">Error: {error}</p>;

    return (
        <div className="max-w-4xl mx-auto px-4 py-6 animate__animated animate__fadeIn">
            <div className="flex items-center justify-between mb-6">
                <div className="flex items-center space-x-3">
                    <img
                        src={config.logoUrl}
                        alt="Logo"
                        className="logo w-8 h-8 rounded-lg"
                    />
                    <h1 className="text-xl font-bold text-gray-900">{config.title}</h1>
                </div>
            </div>

            <Breadcrumb path={path} />
            <div className="grid grid-cols-1 gap-3">
                {files.map((file) => (
                    <FileItem key={file.path} file={file} />
                ))}
            </div>
        </div>
    );
};

export default FileExplorer;