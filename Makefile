NAME = towerdefense
BIN_FOLDER = bin/
DEPS_FOLDER = ".:"

MAIN_PATH = "towerdefense/Launcher"
MAIN_PATH_MANIFEST = "towerdefense.Launcher"

MANIFEST = "manifest.mf"
JAR = $(NAME).jar


all :
	@mkdir -p $(BIN_FOLDER)
	@cd src || exit 0; javac -cp ../$(DEPS_FOLDER) -d ../$(BIN_FOLDER) $(MAIN_PATH).java 
	@echo "Main-Class: "$(MAIN_PATH_MANIFEST) > $(MANIFEST)
	@jar -cvmf $(MANIFEST) $(JAR) -C $(BIN_FOLDER) ./

runAll : all run

run:
	@java -jar $(JAR) $(RUN_ARGS)

clean:
	@rm -rf $(BIN_FOLDER)
	@rm -rf $(MANIFEST)

fclean: clean
	@rm -rf $(JAR)

re : fclean all

momo : 
	make -C . all MAIN_PATH="towerdefense/Launcher" MAIN_PATH_MANIFEST="towerdefense.Launcher"

