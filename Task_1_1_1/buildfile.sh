javac -cp ./junit-platform-console-standalone-1.7.0-all.jar ./src/main/java/ru/nsu/vyaznikova/Main.java ./src/test/java/ru/nsu/vyaznikova/MainTest.java -d ./builddir

if [[ $? -ne 0 ]]; then
    echo "epic fail! (compilation)"
    exit 101
fi

jar -cfm ./main.jar ./Manifest.txt ./builddir/ru/nsu/vyaznikova/Main.class ./builddir/ru/nsu/vyaznikova/Heap.class

if [[ $? -ne 0 ]]; then
    echo "epic fail! (building to jar)"
    exit 102
fi

java -javaagent:"./jacocoagent.jar=destfile=./jacoco_exit.exec" \
    -jar  "./junit-platform-console-standalone-1.7.0-all.jar" -cp ./builddir --scan-classpath

if [[ $? -ne 0 ]]; then
    echo "epic fail! (junit test execution)"
    exit 103
fi

javadoc ./src/main/java/ru/nsu/vyaznikova/Main.java -d ./javadoc

if [[ $? -ne 0 ]]; then
    echo "epic fail! (documentation)"
    exit 104
fi

java -jar ./jacococli.jar report ./jacoco_exit.exec \
    --classfiles ./builddir/ \
    --sourcefiles ./src/main/java \
    --html ./coverage \
    --name "coverage"

if [[ $? -ne 0 ]]; then
    echo "epic fail! (coverage report generation)"
    exit 105
fi

echo "success! (script finished)"
exit 0
