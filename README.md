# StaticFileServer

StaticFileServer is an all-in-one JAR-bundled web server designed to serve assets and large files effortlessly. It uses [Flash](https://github.com/Pixel-Services/flash) for the backend and React + TailwindCSS for the frontend, making it lightweight, efficient, and easy to use.

---

## 🌟 Features

- 📂 **Serve static files** from a specified directory with ease
- 🚀 **Built-in React frontend** for an intuitive user interface
- 🔥 **Flash-based backend** for high performance
- 🎨 **Customizable UI** (logo, favicon, and title)
- ⚙️ **Lightweight and self-contained** (single JAR deployment)
- 🔧 **Configurable via `config.yml`**

---

## 📥 Getting Started

### Option 1: Download Pre-built JAR

1. Download the latest release from the [Releases](https://github.com/Relism/StaticFilesServer/releases) tab.
2. Run the server with:

```sh
java -jar StaticFileServer.jar
```

### Option 2: Build from Source
1. Clone the repository:

```sh
git clone https://github.com/Relism/StaticFilesServer.git
cd StaticFilesServer
```

2. Build the frontend bundles:

```sh
cd frontend
pnpm run build
cd ..
```

3. Build the backend using Gradle:

```sh
./gradlew build
```

4. Run the JAR:

```sh
java -jar build/libs/StaticFileServer.jar
```

## ⚙️ Configuration
When you first run the server, it will generate a config.yml file in the working directory. You can modify it to suit your needs:

```yaml
serverPort: 8080 # Port on which the server will run
filesDir: "/path/to/your/files" # Directory where the files are stored
logoUrl: "" # URL to the logo image (can point to the server itself)
faviconUrl: "" # URL to the favicon image (can point to the server itself)
title: "Static Files Server" # Title of the website
```

Make sure that filesDir points to a valid directory with read access.

## Screenshots 🖼️

Here's a preview of the web interface:

![StaticFileServer Screenshot](https://static.pixel-services.com/static/assets/relism/github/screenshots/staticfileserver_screenshot.png)

### License

StaticFileServer is open-source and licensed under the MIT License.
