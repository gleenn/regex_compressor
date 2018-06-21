default:
	cat Makefile

test:
	mvn test

set-version:
	echo "this doesn't work yet"
	exit
	test -n "$(VERSION)" # $$VERSION is required
	sed -i 's/<version>0</version>/<version>$$VERSION</version>/g' pom.xml

deploy: set-version
	echo "I hope you bumped the version number!"
	git push
	mvn test deploy -X
