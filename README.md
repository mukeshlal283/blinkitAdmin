# Blinkit Clone App (For Admin)

This is the Admin App for the Blinkit Clone project. It allows the admin to manage the product catalog in real-time. The admin can add, update, and delete products along with managing their details like price, quantity, and images.

## Features - 
* Admin Authentication – Secure login for admins only
* Add New Products – Upload product details (name, category, description)
* Upload Images – Pick from gallery / capture using camera and save in database/storage
* Set Price – Define product pricing
* Manage Quantities – Add stock, update stock, or set availability
* Update or Delete Products – Edit details anytime
* Real-time Sync – Products instantly reflect in the User App

## Tech Stack

* Architecture: MVVM
* Database: Firebase Firestore / Realtime Database (for products)
* Storage: Firebase Storage (for images)
* Networking: Retrofit (if using backend APIs)
* UI: RecyclerView + Adapter for displaying products
* Other: Glide/Picasso for image loading

## Flow - 
* Flow
* Admin logs in with credentials
* Admin can add new product → enter details → upload image → set price & quantity
* Products are stored in Firebase (or custom backend)
* User app fetches the latest product list in real-time