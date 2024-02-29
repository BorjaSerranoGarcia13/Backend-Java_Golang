db = db.getSiblingDB(process.env.MONGO_INITDB_DATABASE);
db.createUser(
  {
    user: "user",
    pwd: "password",
    roles: [ { role: "readWrite", db: process.env.MONGO_INITDB_DATABASE } ]
  }
);