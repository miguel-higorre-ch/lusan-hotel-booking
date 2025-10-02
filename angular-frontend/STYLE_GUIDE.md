### 1. Global Styles (`src/styles.css`)

I have created a global stylesheet that provides a consistent and modern design across your entire application. This file is located at `src/styles.css` and is already loaded by your application.

**How to Use It:**

The global styles are designed to be automatically applied to standard HTML elements. For custom components, you can leverage the CSS utility classes I've created.

*   **Containers**: To center and constrain the width of your content on a page, wrap it in a `div` with the `.container` class.

    ```html
    <div class="container">
      <!-- Your page content here -->
    </div>
    ```

*   **Buttons**: Use the `.btn` and `.btn-primary` classes on your `<button>` or `<a>` elements for consistent button styling.

    ```html
    <button class="btn btn-primary">Click Me</button>
    <a routerLink="/some-route" class="btn btn-primary">Go to Page</a>
    ```

*   **Cards**: The `.card` class is perfect for creating content containers with a background, padding, and shadow.

    ```html
    <div class="card">
      <h3>Card Title</h3>
      <p>This is some content inside a card.</p>
    </div>
    ```

*   **Forms**: Structure your forms using `.form-group` for spacing and `.form-control` for input fields.

    ```html
    <div class="form-group">
      <label for="name">Name</label>
      <input type="text" id="name" class="form-control">
    </div>
    ```

*   **Tables**: For data tables, like in your admin section, add the `.table` class to your `<table>` element.

    ```html
    <table class="table">
      <thead>
        ...
      </thead>
      <tbody>
        ...
      </tbody>
    </table>
    ```

### 2. Route Animations

I have configured a fade-in/fade-out animation that runs automatically when you navigate between pages.

**How to Use It:**

This is already fully configured. The `animations.ts`, `app.config.ts`, `app.ts`, and `app.html` files have been updated to make this work out of the box. You do not need to do anything further to enable the animations.

### 3. Component Styling Examples

Here’s how you can apply the new styles to your components:

**Navbar (`src/app/navbar/`)**

I have already refactored the navbar for you. It is now responsive and uses the new styles. It will automatically collapse into a hamburger menu on mobile devices.

**Home Page**

To create a visually appealing home page, you can add a "hero" section.

1.  **In `home.html`:**

    ```html
    <div class="hero">
      <div class="hero-content">
        <h1>Welcome to Lusan Hotel</h1>
        <p>Your perfect getaway starts here.</p>
        <a routerLink="/rooms" class="btn btn-primary">Explore Rooms</a>
      </div>
    </div>

    <div class="container">
      <!-- Your room search component or other content -->
      <app-roomsearch></app-roomsearch>
    </div>
    ```

2.  **In `home.css`:**

    ```css
    .hero {
      background-image: url('/public/images/hotelbg.jpg'); /* Ensure this path is correct */
      background-size: cover;
      background-position: center;
      height: 60vh;
      display: flex;
      align-items: center;
      justify-content: center;
      text-align: center;
      color: var(--white);
    }

    .hero-content {
      background-color: rgba(0, 0, 0, 0.5);
      padding: 2rem;
      border-radius: var(--border-radius);
    }
    ```

**Admin Pages (e.g., `manage-rooms.html`)**

To style your admin data tables, simply add the `.table` class.

```html
<div class="container">
  <h2>Manage Rooms</h2>
  <button class="btn btn-primary" routerLink="/admin/add-room">Add New Room</button>

  <table class="table">
    <thead>
      <tr>
        <th>Room Type</th>
        <th>Price</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <!-- Example Row -->
      <tr>
        <td>Single Room</td>
        <td>$100</td>
        <td>
          <a routerLink="/admin/edit-room/1">Edit</a>
          <button class="btn">Delete</button>
        </td>
      </tr>
    </tbody>
  </table>
</div>
```

By following these guidelines, you can apply the new design system consistently across your application, resulting in a professional and polished user experience.



Here is the final documentation for the styles I have applied.

  ### Global Styles (src/styles.css)

  This is the foundation of your site's new look. It contains:


   * CSS Variables: For consistent colors, shadows, and border-radius across the application.
  I have refactored your components to use a modular and consistent styling approach. Here’s how it’s structured:

  #### 1. Authentication Pages (/login, /register)


   * CSS: These components now share a single stylesheet: src/app/auth.css.
  `html
      <div class="auth-container">
        <div class="card auth-card">
          <h2>Login</h2>
          <form class="auth-form">
            <div class="form-group">
              <label for="email">Email</label>
              <input id="email" type="email" class="form-control">
            </div>
            ...
            <button type="submit" class="btn btn-primary">Login</button>
          </form>
        </div>
      </div>
      `

  #### 2. Admin Form Pages (/admin/add-room, /admin/edit-room, etc.)


   * CSS: All form-based admin pages now share a single stylesheet: src/app/admin/admin-forms.css.
  `html
      <div class="container admin-form-container">
        <div class="card">
          <div class="admin-form-header">
            <h2>Add New Room</h2>
          </div>
          <form>
            <div class="form-group">
              <label for="roomType">Room Type</label>
              <input id="roomType" type="text" class="form-control">
            </div>
            ...
            <button type="submit" class="btn btn-primary">Save Room</button>
          </form>
        </div>
      </div>
      `

  #### 3. Admin Table Pages (/admin/manage-rooms, /admin/manage-bookings)


   * CSS: These pages now use the shared src/app/admin/admin-tables.css stylesheet.
  `html
      <div class="container admin-table-container">
        <div class="admin-table-header">
          <h2>Manage Rooms</h2>
          <a routerLink="/admin/add-room" class="btn btn-primary">Add New Room</a>
        </div>
        <table class="table">
          ...
        </table>
      </div>
      `
      (Note: For `manage-rooms`, we are using the already-styled `app-roomresult` component which displays cards instead of a table, but it follows the same header 
  and container structure).

  #### 4. Find Booking Page (/findbooking)

   * CSS: This page also uses the src/app/admin/admin-forms.css for consistency in its search field and results display.
   * Consolidated Styles: I have removed the individual, duplicated CSS from each component and replaced it with shared, reusable stylesheets (auth.css, 
     admin-forms.css, admin-tables.css).
  This new structure makes your styles easier to maintain and ensures a consistent, professional look across your entire application. If you create new components, 
  you can now easily apply these styles by referencing the appropriate shared stylesheet and using the documented HTML structure.


  I believe this completes all of your styling requests. Let me know if there is anything else I can help with.