# IBM-PSM-With-Watson
IBM PSM With Watson (a Vue.js + Spring Boot + BangDB web application) As Tianang Chen's UCL MEng Final Year Project

## Project Title 

**Using IBM software and Watson (Watson Assistant) through a voice gateway to call patients to schedule/reschedule appointments**

## System Manual

The web application has been deployed at <http://43.157.89.132>.

To run the application locally, follow the steps:

- Clone the repository **git clone https://github.com/akalisvm/IBM-PSM-With-Watson.git**.
- Run **cd vue** to switch to the root package of the front end. 
- Run **npm run serve** to start the front end.
- Run **main()** method in the class **IbmPsmWithWatsonApplication** to start back end.
- Go to <http://localhost:8080> to start using the application locally.

The API key, URL, and Environment ID for IBM Watson Assistant and IBM Speech to Text are concealed to prevent unauthorized access to credentials. Similarly, the email and password for the SMTP service are also obscured to avoid malicious usage, implying that the further developer needs to create their own SMTP service. This approach ensures security and protects against potential breaches or misuse of sensitive information.