const PROXY_CONFIG = [
	{
		context: [
			"/ServeurDrive",
			"ServeurDrive/Files"
		],
		target : "http://localhost:8080",
		secure: false,
	}
]

module.exports = PROXY_CONFIG
