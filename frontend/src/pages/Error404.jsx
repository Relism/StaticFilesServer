// Error404.jsx
const Error404 = () => (
    <div className="error-container">
        <img src="https://static.pixel-services.com/static/assets/wemx/moonlogo_cropped.png" alt="Logo" className="logo w-16 h-16 mx-auto" />
        <div className="error-code text-gray-900 dark:text-gray-100">404</div>
        <div className="error-message text-gray-600 dark:text-gray-400">Oops! The page you're looking for doesn't exist.</div>
        <a href="/" className="home-link">Go Back Home</a>
    </div>
);

export default Error404;