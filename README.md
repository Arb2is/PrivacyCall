# PrivacyCall
Creada para solucionar un problema en las conversaciones privadas entre usuarios, donde existe la posibilidad de que se filtre informacion de las personas, por utilizar dispositivos móviles o fijos, bluethooth en el audio del vehiculó o altavozes portatil y manos libre tambien otros dispositivos, donde podría escuchar una tercera persona que no tiene autorizacion en la conversación, sin un previo aviso que esta situación esta pasando por diferentes motivos o razones, donde se vulnera la privacidad de la conversación, afectando la confianza entre los usuarios, la seguridad compartida restablece la confianza perdidad 

## Cómo Ejecutar la Aplicación

### 1. Aplicación Android (PrivacyCall)

Para ejecutar la aplicación Android, necesitarás un emulador de Android o un dispositivo Android conectado.

1.  **Construir e Instalar la Aplicación:**
    Abre una terminal en la raíz del proyecto (c:\Users\DENOB\Desktop\PrivacyCall\PrivacyCall-2\) y ejecuta el siguiente comando:

    ```bash
    ./gradlew :androidApp:installDebug
    ```
    (En Windows, usa `gradlew.bat :androidApp:installDebug`)

    Este comando construirá la aplicación y la instalará en tu emulador o dispositivo conectado.

2.  **Iniciar la Aplicación:**
    Una vez instalada, puedes iniciar la aplicación "PrivacyCall" manualmente desde el lanzador de aplicaciones de tu emulador o dispositivo.

### 2. Servidor Broker (broker.js)

El servidor broker es una aplicación Node.js.

1.  **Asegúrate de tener Node.js instalado.** Si no lo tienes, puedes descargarlo desde [nodejs.org](https://nodejs.org/).

2.  **Ejecutar el Servidor:**
    Abre una terminal en la raíz del proyecto (c:\Users\DENOB\Desktop\PrivacyCall\PrivacyCall-2\) y ejecuta el siguiente comando:

    ```bash
    node broker.js
    ```