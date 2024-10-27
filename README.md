# CarHub Rentals
## Project Description
CarHub Rentals is a mobile application designed to facilitate easy car rentals, allowing users to browse a range of cars, book rentals, and manage bookings effectively. Users can view rental costs, confirm bookings, and track order history with options for cancellation. This application ensures a seamless user experience with a secure authentication system and offline order history management.

## Technologies Used
- **Frontend:** Java, XML
- **Backend & Authentication:** Firebase Authentication and Realtime Database
- **Database for Orders:** SQLite
- **Layout & UI:** Android SDK, Constraint Layout, Card View

## Features
- **User Authentication**:
  - Register, login, and logout using Firebase Authentication.
- **Browse Cars**:
  - View a wide selection of cars, including details like fuel type, transmission, mileage, and pricing.
- **Booking System**:
  - Book a car with a calculated price based on rental dates.
  - Real-time booking confirmation message.
- **Order History & Management**:
  - Track past orders stored in SQLite for offline access.
  - Option to cancel bookings with confirmation prompts.
- **UI Navigation**:
  - Bottom navigation bar for quick access between Home and Profile sections.
  
## Installation
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/CarHub.git
   cd CarHub
2. **Open in Android Studio**:
   - Open Android Studio, go to File > Open, and select the cloned project.
3. **Set Up Firebase**:
   - Configure Firebase for authentication and database functionalities.
   - Add google-services.json (downloaded from the Firebase Console) to the app/ directory.
4. **Build the Project**:
   - Sync Gradle and build the project in Android Studio.
5. **Database Setup**:
   - The SQLite database for order history is created automatically within the app.

## Usage
1. **User Registration and Login**:
   - New users can register or log in with existing credentials.
2. **Browse Cars**:
   - View available cars on the Home page by selecting "Browse Cars".
3. **Book a Car**:
   - Choose a car, specify rental dates, and confirm booking with calculated costs.
4. **View Orders**:
   - Go to the Profile section to view and manage past bookings.
5. **Cancel Order**:
   - Cancel any booking from the order history with a confirmation prompt.

##License
