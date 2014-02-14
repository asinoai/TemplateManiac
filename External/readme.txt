Contains libraries shared by modules at compilation time.

As long as libraries are backward compatible , we can keep them here.
Its it happens to change , we just have to copy prior version in modules /lib and change their classpath accordingly.
The benefit is that as long as this assumption is valid , we avoid copying everything.
This backward compatibility assumption is of course valid for the JDK.
