# How to setup a development environment?

1.Install tomcat7

Open myeclipse

1.Set the encoding of Workspace to UTF-8
Window>>Preferences>>General>>Workspace>>Text file encoding=UTF-8
Otherwise, the diff tool in Egit would get problem.

2.Set the end-of-line sequence setting to be unix
Window > Preferences > General > Workspace > "New text file line delimiter".
Otherwise, the diff tool in Egit would get problem.

2.Set the encoding of tomcat 7 to UTF-8

update tomcat7 folder: conf/server.xml
<Connector port="8080" protocol="HTTP/1.1"  
              connectionTimeout="20000"  
              redirectPort="8443" URIEncoding="UTF-8"/>  
			  
Otherwise, the Chinese input text from page will be garbled when received by JSP

# How to deploy?

1