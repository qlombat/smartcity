window.onload = function () {
    // Build a system
    const ui = SwaggerUIBundle({
        url: window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + window.location.pathname + ".json",
        dom_id: '#swagger-ui',
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIStandalonePreset
        ],
        plugins: [
            SwaggerUIBundle.plugins.DownloadUrl
        ],
        layout: "StandaloneLayout"
    })

    window.ui = ui
}