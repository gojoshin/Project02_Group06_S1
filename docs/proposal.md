# Moviebox API Proposal

## 1. The pitch (one paragraph)
The api would pull movie and show data including reviews, how long it is, each episode, similar movies. 
People who like to watch movies and tv shows. The app would make it easier to keep track of 
everything since it would all be in one place.

## 2. Resources
| Resource       | Key fields | Relationships |
|----------------|---|---|
| User           | id, username, email, role | a User has many Lists, Favorites, Ratings, and a Watchlist |
| Movie          | id, title, overview, runtime, release_date, poster_path, backdrop_path | a Movie has many Reviews, Images, and Similar Movies |
| Session        | session_id, user_id, expires_at | a Session belongs to a Userkdrop_path | a TV Show has many Seasons, Episodes, Reviews, Images |
| Episode        | id, name, episode_number, season_number, runtime, air_date | an Episode belongs to a TV Show |
| Review         | id, author, content, rating, created_at | a Review belongs to a Movie, TV Show, or Episode |
| Image          | file_path, width, height, aspect_ratio | an Image belongs to a Movie or TV Show |
| List           | id, name, description, user_id | a List belongs to a User and has many Movies/TV Shows |
| Watchlist Item | id, user_id, media_type, media_id | a Watchlist Item belongs to a User and references a Movie/TV Show |
| Favorite Item  | id, user_id, media_type, media_id | a Favorite Item belongs to a User and references a Movie/TV Show |
| Rating         | id, user_id, media_type, media_id, score | a Rating belongs to a User and references a Movie, TV Show, or Episode |
| Session        | session_id, expires_at | a Session belongs to a User |



## 3. ER sketch
Tables, primary and foreign keys, and cardinality. Edit this Mermaid diagram (it renders on GitHub;
try changes at https://mermaid.live):

```mermaid
erDiagram
    USER_ACCOUNT ||--o{ SESSION : has
    USER_ACCOUNT ||--o{ LIST : creates
    USER_ACCOUNT ||--o{ WATCHLIST_ITEM : owns
    USER_ACCOUNT ||--o{ FAVORITE_ITEM : owns
    USER_ACCOUNT ||--o{ RATING : gives

    MOVIE ||--o{ REVIEW : receives
    TV_SHOW ||--o{ REVIEW : receives
    EPISODE ||--o{ REVIEW : receives

    MOVIE ||--o{ IMAGE : has
    TV_SHOW ||--o{ IMAGE : has

    TV_SHOW ||--o{ SEASON : contains
    SEASON ||--o{ EPISODE : contains

    LIST ||--o{ LIST_ITEM : contains
    MOVIE ||--o{ LIST_ITEM : appears_in
    TV_SHOW ||--o{ LIST_ITEM : appears_in

    MOVIE ||--o{ WATCHLIST_ITEM : added_to
    TV_SHOW ||--o{ WATCHLIST_ITEM : added_to

    MOVIE ||--o{ FAVORITE_ITEM : favorited
    TV_SHOW ||--o{ FAVORITE_ITEM : favorited

    MOVIE ||--o{ RATING : rated
    TV_SHOW ||--o{ RATING : rated
    EPISODE ||--o{ RATING : rated

    MOVIE }o--o{ MOVIE : similar_to

    USER_ACCOUNT {
        bigint id PK
        string username
        string email UK
        string role
    }

    SESSION {
        string session_id PK
        bigint user_id FK
        datetime expires_at
    }

    MOVIE {
        bigint id PK
        string title
        string overview
        int runtime
        date release_date
        string poster_path
        string backdrop_path
    }

    TV_SHOW {
        bigint id PK
        string title
        string overview
        date first_air_date
        string poster_path
        string backdrop_path
    }

    SEASON {
        bigint id PK
        bigint tv_show_id FK
        int season_number
        string name
    }

    EPISODE {
        bigint id PK
        bigint season_id FK
        string name
        int episode_number
        int season_number
        int runtime
        date air_date
    }

    REVIEW {
        bigint id PK
        string author
        string content
        float rating
        datetime created_at
        string media_type
        bigint media_id
    }

    IMAGE {
        string file_path PK
        int width
        int height
        float aspect_ratio
        string media_type
        bigint media_id
    }

    LIST {
        bigint id PK
        string name
        string description
        bigint user_id FK
    }

    LIST_ITEM {
        bigint id PK
        bigint list_id FK
        string media_type
        bigint media_id
    }

    WATCHLIST_ITEM {
        bigint id PK
        bigint user_id FK
        string media_type
        bigint media_id
    }

    FAVORITE_ITEM {
        bigint id PK
        bigint user_id FK
        string media_type
        bigint media_id
    }

    RATING {
        bigint id PK
        bigint user_id FK
        string media_type
        bigint media_id
        float score
    }

```

## 4. Endpoints
| Verb | Path | Auth | Purpose |
|---|---|---|---|
| GET | /api/v1/workouts?page=0&size=20 | user | list my workouts (paginated) |
| ... | ... | ... | ... |
Mark each endpoint `public`, `user`, or `admin`. Mark which collection paginates and which
filters or sorts.

## 5. Technical choices
- **Database host:** Supabase
- **OAuth2 provider:** Google
- **Repo layout:** Monorepo, we want to keep everything in one repository.

- These become your ADRs later.

## 6. Risks
1. **Third-Party API Rate Limiting & Image Performance:** Since the app relies heavily on the TMDB API for data and loading multiple high-resolution images (posters and backdrops) in scrolling lists, we risk hitting rate limits or causing out-of-memory (OOM) errors on the device.
    *   *How we will find out/mitigate:* In Sprint 1, we will build a prototype using an image loading library (like Coil or Glide) and the Paging 3 library to fetch and display a list of movies. We will monitor memory usage and ensure we request appropriately sized thumbnails instead of original image sizes.
2. **State Synchronization for User Data:** Keeping the user's local UI state (like hearting a favorite movie or adding to a watchlist) in sync with the remote server/API, especially across multiple screens or offline, can lead to buggy UI.
    *   *How we will find out/mitigate:* We will create a small technical spike in Sprint 1 to test an architecture using a local database (Room) as a "single source of truth". We will verify if updates to local data correctly trigger UI refreshes via Kotlin StateFlow before syncing back to the network.

## 7. Team and Sprint 1
Who owns what in Sprint 1. 

Jayson is working on the frontend UI and database.

Daniel is working on populating the explore page.

Joseph is working on tracking individual episodes of a show.

Joshua is working on the API and backend.


**Project board:** https://github.com/users/gojoshin/projects/2

**Sprint 1 milestone:** https://github.com/gojoshin/Project02_Group06_S1/milestones
