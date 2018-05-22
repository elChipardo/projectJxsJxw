const PROXY_CONFIG = [
	{
		context: [
			"/ServeurDrive",
			"ServeurDrive/Files",
			"ServeurDrive/SpaceGoogleDrive",
			"ServeurDrive/SpaceDropBox",
			"ServeurDrive/Move",
			"/ServeurDrive/Childrens",
			"ServeurDrive/Upload",
			"ServeurDrive/DownloadGoogleDrive"

		],
		target : "http://localhost:8080",
		secure: false,
	}
]

module.exports = PROXY_CONFIG
