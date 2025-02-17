import React from 'react';
import { Link } from 'react-router-dom';

const Breadcrumb = ({ path }) => {
    const parts = path ? path.split('/') : [];
    let currentPath = '';

    return (
        <div className="flex items-center space-x-2 text-sm text-gray-500 dark:text-gray-400 mb-4">
            <Link to="/" className="hover:text-blue-500 dark:hover:text-blue-400 transition-colors">
                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                </svg>
            </Link>
            {parts.map((part, index) => {
                currentPath = currentPath === '' ? part : `${currentPath}/${part}`;
                return (
                    <React.Fragment key={index}>
                        <span className="text-gray-400 dark:text-gray-500">/</span>
                        <Link to={`/${currentPath}`} className="hover:text-blue-500 dark:hover:text-blue-400 transition-colors truncate">
                            {part}
                        </Link>
                    </React.Fragment>
                );
            })}
        </div>
    );
};

export default Breadcrumb;