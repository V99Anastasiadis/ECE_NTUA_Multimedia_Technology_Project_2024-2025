#!/bin/bash

# Ορισμός ονόματος φακέλου του project
PROJECT_DIR="task-manager"

echo "🔄 Έλεγχος ύπαρξης του φακέλου $PROJECT_DIR..."
if [ ! -d "$PROJECT_DIR" ]; then
    echo "❌ Σφάλμα: Δεν βρέθηκε ο φάκελος $PROJECT_DIR!"
    exit 1
fi

echo "📂 Μετάβαση στο project directory..."
cd "$PROJECT_DIR" || { echo "❌ Σφάλμα: Αποτυχία μετάβασης στο $PROJECT_DIR"; exit 1; }

echo "🧹 [STEP 1] Καθαρισμός παλιών αρχείων..."
mvn clean || { echo "❌ Σφάλμα στο mvn clean"; exit 1; }

echo "🛠 [STEP 2] Μεταγλώττιση του project..."
mvn compile || { echo "❌ Σφάλμα στο mvn compile"; exit 1; }

echo "📦 [STEP 3] Δημιουργία package..."
mvn package || { echo "❌ Σφάλμα στο mvn package"; exit 1; }

echo "🚀 [STEP 4] Εκτέλεση του project..."
mvn exec:java -Dexec.mainClass="com.taskmanager.Main" || { echo "❌ Σφάλμα στην εκτέλεση του προγράμματος"; exit 1; }

echo "✅ Το script εκτελέστηκε επιτυχώς!"
