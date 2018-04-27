const PROXY_CONFIG = [
	{
		context: [
			"ServeurDrive/Google/Oauth",
			"ServeurDrive/DropBox/Oauth"
		],
		target : "http://localhost:8080",
		secure: false,
	}
]

module.exports = PROXY_CONFIG