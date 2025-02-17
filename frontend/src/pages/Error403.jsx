const Error403 = ({ logoUrl }) => (
    <div className="error-container">
        <img src={logoUrl} alt="Logo" className="logo w-16 h-16 mx-auto" />
        <div className="error-code text-gray-900">403</div>
        <div className="error-message text-gray-600">Oops! You don't have permission to access this page.</div>
        <a href="/" className="home-link">Go Back Home</a>
    </div>
);

export default Error403;