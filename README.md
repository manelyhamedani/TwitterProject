# Twitter Project - Advanced Programming (AP) Course

**Author:** Manely Ghasemnia Hamedani  
**Course:** Advanced Programming (AP)  

## Overview

This project is an implementation of a simplified version of a Twitter-like social media platform. The project aims to demonstrate the skills and concepts learned during the Advanced Programming (AP) course. The functionalities implemented in this project are related to user management, tweet interactions, and basic social media features such as following users, liking tweets, and replying to tweets.

## Features

The following features are included in this project:

1. **User Management**:
   - User login and signup
   - Setting user information and profile picture

2. **Follow System**:
   - Follow and unfollow users
   - List followers and following

3. **Tweet Management**:
   - Post tweets, retweet, and quote tweets
   - Like and unlike tweets
   - Reply to tweets

4. **Blocking System**:
   - Block and unblock users

5. **Timeline and Filter**:
   - View personal timeline
   - Filter tweets

6. **Voting**:
   - Vote on tweets

## API Endpoints

The following API endpoints are used for the functionalities:

- **Login**: `/login`
- **Signup**: `/signup`
- **Set User Info**: `/set-user-info`
- **Set User Image**: `/set-user-image`
- **Follow**: `/follow`
- **Unfollow**: `/unfollow`
- **List Followers**: `/list-followers`
- **List Following**: `/list-following`
- **Search**: `/search`
- **Post Tweet**: `/tweet`
- **Retweet**: `/retweet`
- **Quote Tweet**: `/quote`
- **Like**: `/like`
- **Unlike**: `/unlike`
- **Reply**: `/reply`
- **Block**: `/block`
- **Unblock**: `/unblock`
- **Timeline**: `/timeline`
- **Filter Tweets**: `/filter`
- **Fetch User Posts**: `/fetch-user-posts`
- **Fetch Tweet Replies**: `/fetch-tweet-replies`
- **Vote**: `/vote`

## Setup Instructions

### Prerequisites

- Java 8 or above
- IDE (e.g., IntelliJ IDEA, Eclipse) or any text editor

### Installation

1. Clone the repository or download the project files.
2. Open the project in your chosen IDE.
3. Ensure the server is configured to run on port `7070` (as defined in the `API.java`).
4. Set up the necessary server configurations to handle the requests defined in the `API.java`.
5. Run the application.

### Run the Project

- Compile and run the Java application on your server.
- Use any HTTP client or front-end application to interact with the APIs.

## License

This project is licensed under the MIT License - see the [MIT License](https://opensource.org/licenses/MIT) for details.

## Acknowledgements

- Special thanks to the course instructors and peers for feedback and support during the development of this project.
