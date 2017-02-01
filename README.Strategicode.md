How do build & deploy
=====================

cd boon
mvn test
cd ..
mvn deploy

// TODO: Exclude non-core boon
INFO] Boon and subprojects .............................. SUCCESS [7.109s]
[INFO] osgi-plugins-parent ............................... SUCCESS [4.385s]
[INFO] boon .............................................. SUCCESS [43.776s]
[INFO] bnsf-core ......................................... FAILURE [12.921s]
[INFO] etcd-bundle ....................................... SKIPPED
[INFO] ETCD Client ....................................... SKIPPED

20170201: Had to put credentials in ~/m2/settings.xml for it to work.
          Was getting errors: "Unable to load AWS credentials from any provider in the chain"

