from passlib.hash import bcrypt

htpasswd_entries = {
    "user-1": "$2b$12$xC/PzSyy0bwrwlPwYA.2o.7rpMzlsEAJ5uD/U4uAvgL0hohMrEzIC",
    "user-2": "$2b$12$T.nFoac6o4Ahan52dQCJFuXfQAGvk48X6QHnBgufi89QhEkYQ9DFG",
    "user-3": "$2b$12$uGfRbRNaGosw9lwasx8FrOXYuvUe9FFd8Aab3P8gHfBleKzu0BRCe"
}

# Intenta con las contraseñas esperadas (ajústalas si son otras)
passwords = {
    "user-1": "hack-user-1",
    "user-2": "hack-user-2",
    "user-3": "hack-user-3"
}

for user, hash_ in htpasswd_entries.items():
    password = passwords.get(user)
    if bcrypt.verify(password, hash_):
        print(f"{user}: ✅ contraseña válida")
    else:
        print(f"{user}: ❌ contraseña inválida")
