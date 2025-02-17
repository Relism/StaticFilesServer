import React from 'react';
import { Link } from 'react-router-dom';

const FileItem = ({ file }) => {
    const isDirectory = file.type === 'directory';
    const filePath = isDirectory ? `/${file.path}` : `/static/${file.path}`;

    return isDirectory ? (
        <Link
            to={filePath}
            className="file-item group bg-white dark:bg-gray-800 rounded-lg p-3 flex items-center space-x-3 hover:shadow-lg transition"
        >
            <FileIcon file={file} />
            <FileInfo file={file} />
        </Link>
    ) : (
        <a
            href={filePath}
            className="file-item group bg-white dark:bg-gray-800 rounded-lg p-3 flex items-center space-x-3 hover:shadow-lg transition relative"
            download
        >
            <FileIcon file={file} />
            <FileInfo file={file} />
            <DownloadIcon />
        </a>
    );
};

const FileIcon = ({ file }) => (
    <div
        className={`w-8 h-8 rounded-lg ${
            file.type === 'directory' ? 'bg-blue-500/10 dark:bg-blue-500/20' : 'bg-gray-200 dark:bg-gray-700'
        } flex items-center justify-center`}
    >
        {file.type === 'directory' ? (
            <svg className="w-5 h-5 text-blue-500 dark:text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-6l-2-2H5a2 2 0 00-2 2z" />
            </svg>
        ) : (
            <svg className="w-5 h-5 text-gray-600 dark:text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
        )}
    </div>
);

const FileInfo = ({ file }) => (
    <div className="flex-1 min-w-0">
        <div className="text-sm font-medium text-gray-900 dark:text-gray-100 truncate">{file.name}</div>
        <div className="text-xs text-gray-500 dark:text-gray-400 mt-1">
            {file.type === 'directory' ? 'Directory' : `File - ${formatFileSize(file.size)}`}
        </div>
    </div>
);

const DownloadIcon = () => (
    <svg className="download-icon" width="20px" height="20px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path
            d="M3 15C3 17.8284 3 19.2426 3.87868 20.1213C4.75736 21 6.17157 21 9 21H15C17.8284 21 19.2426 21 20.1213 20.1213C21 19.2426 21 17.8284 21 15"
            stroke="#1C274C" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        <path d="M12 3V16M12 16L16 11.625M12 16L8 11.625" stroke="#1C274C" stroke-width="1.5" stroke-linecap="round"
              stroke-linejoin="round"/>
    </svg>
);

const formatFileSize = (bytes) => {
    if (bytes < 1024) return bytes + ' B';
    let kb = bytes / 1024;
    if (kb < 1024) return kb.toFixed(1) + ' KB';
    let mb = kb / 1024;
    if (mb < 1024) return mb.toFixed(1) + ' MB';
    let gb = mb / 1024;
    return gb.toFixed(1) + ' GB';
};

export default FileItem;