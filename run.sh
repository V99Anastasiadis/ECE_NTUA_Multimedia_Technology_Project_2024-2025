#!/bin/bash

# Ορισμός ονόματος φακέλου του project
PROJECT_DIR="task-manager"

echo " Έλεγχος ύπαρξης του φακέλου $PROJECT_DIR..."
if [ ! -d "$PROJECT_DIR" ]; then
    echo " Σφάλμα: Δεν βρέθηκε ο φάκελος $PROJECT_DIR!"
    exit 1
fi

echo " Μετάβαση στο project directory..."
cd "$PROJECT_DIR" || { echo " Σφάλμα: Αποτυχία μετάβασης στο $PROJECT_DIR"; exit 1; }

echo " [STEP 1] Καθαρισμός παλιών αρχείων..."
mvn clean || { echo " Σφάλμα στο mvn clean"; exit 1; }

echo " [STEP 2] Μεταγλώττιση του project..."
mvn compile || { echo " Σφάλμα στο mvn compile"; exit 1; }

echo " [STEP 3] Δημιουργία package..."
mvn package || { echo " Σφάλμα στο mvn package"; exit 1; }

#  Μετά το build, ρωτάμε τον χρήστη ποιο UI θέλει να εκτελέσει
echo ""
echo " Επιλογές Εκτέλεσης:"
echo "1) GUI (JavaFX)"
echo "2) Terminal Interface"
echo -n " Επίλεξε μια επιλογή (1 ή 2): "
read choice

# Εκτέλεση ανάλογα με την επιλογή του χρήστη
case $choice in
    1)
        echo " Εκτέλεση GUI..."
        mvn exec:java -Dexec.mainClass="com.taskmanager.Main" || { echo "❌ Σφάλμα στην εκτέλεση του GUI"; exit 1; }
        ;;
    2)
        echo " Εκτέλεση Terminal Interface..."
        mvn exec:java -Dexec.mainClass="com.taskmanager.Main" -Dexec.args="terminal" || { echo "❌ Σφάλμα στην εκτέλεση του Terminal Interface"; exit 1; }
        ;;
    *)
        echo " Μη έγκυρη επιλογή! Παρακαλώ εκτέλεσε ξανά το script και επέλεξε 1 ή 2."
        exit 1
        ;;
esac

echo " Το script εκτελέστηκε επιτυχώς!"
