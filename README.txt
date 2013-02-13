UGent Sip Creator 1.0
=======================

1. Introduction
   ============

    This software provides a graphical user interface for these
    tree tasks:

      1. perform renaming operation on files
      2. create bagits, with a mets.xml as tag file to describe the metadata
      3. create an export of this bagit for a specific digital repository
          that does not support bagit (e.g. DSpace).   

    As base for this project, the source code of Bagger (2.1.2) from the Library of Congress
    was used (http://sourceforge.net/projects/loc-xferutils/files/loc-bagger/).
    Hence the name of this git repository 'Bagger-LC'.

    Several changes were made to suit the needs of this project.

    For more information about the use of this program, see:

      - handleiding.docx (Dutch version)
      - http://www.projectcest.be/index.php/UGent_SIP_Creator:Wie%3F_Wat%3F_Hoe%3F_Waarom%3F (Dutch version)

2. License
   =======

    License and other releated information are listed in the LICENSE.txt and NOTICE.txt

3. Build Process
   ====================

  To compile the source code of this application Maven 2.2.1+ and Java 1.6.xx are required.

  Instructions:

    cd Bagger-LC
    mvn clean assembly:assembly
    java -jar target/ugent-sip-1.0-jar-with-dependencies.jar
