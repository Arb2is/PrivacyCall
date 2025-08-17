# SpeakPriv

Aplicación Android para proteger la privacidad durante las llamadas.

## Cómo probar
1. Abre el proyecto en Android Studio y conecta uno o dos dispositivos (o emuladores).
2. Ejecuta `./gradlew :androidApp:installDebug` para compilar e instalar.
3. Concede el rol **Call Screening** al abrir la app.
4. Realiza una llamada para iniciar el servicio de privacidad.
5. Opcional: para pruebas remotas usa un túnel WebSocket con [LocalTunnel](https://localtunnel.github.io/www/).

La aplicación no envía audio ni datos sensibles; solo comparte un indicador binario sobre el uso de altavoz.
