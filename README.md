
google groups exporter [DRAFT]
==============================

This project is designed to export messages from a google group.

The original idea was to copy the raw messages, in order to load them into another library [TODO: add link], designed to expose API to simplyin navigating over them.

The messages are exported as single files in `.eml` format, using the folder structure:

```bash
/{export_dir}/{topic_id}/{msg_id}.eml
```

**NOTE**: this version is very work-in-progress: the original single scala script has been refactorized to different classes, to enable concurrent downloads.


## TODO

0. fix errors / exceptions handling
0. rewrite main classes into proper unit tests
0. push the development branch for AKKA version
0. publish the companion library for API / navigation

----


[![I Love Open Source](http://www.iloveopensource.io/images/logo-lightbg.png)](http://www.iloveopensource.io/users/seralf)
