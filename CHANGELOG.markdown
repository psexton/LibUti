# LibUti Changelog

Version numbers are assigned according to the Semantic Versioning Specification <http://semver.org>.

## Version 1.1.1 (2018-04-07)
- Upgrade to JDOM 2
- Switch to Maven 3 for builds

## Version 1.1.0 (2017-01-06):
- Add JSON and YAML to the standard type database

## Version 1.0.1 (2016-12-14):
- Fixes problem with `UtiBuilder.fromString` and dyn UTIs ([#5](https://github.com/psexton/LibUti/issues/5))

## Version 1.0.0 (2015-09-21):
- Rebuilt using Java 7
- Adds support for reverse mapping UTI to file suffix ([#4](https://github.com/psexton/LibUti/issues/4))
- Adding Markdown and Javascript to the standard type database
- `Uti` implements `Comparable`
- Add `Uti#getConformances` and `Uti#getConformers` methods
- `UtiDb` has public access

## Version 0.2.0 (2013-11-11):
- Adds `Uti#conformsTo` method ([#1](https://github.com/psexton/LibUti/issues/1))
- Fixes problem with uppercase file suffixes ([#2](https://github.com/psexton/LibUti/issues/2))
- Adds some additional file types ([#3](https://github.com/psexton/LibUti/issues/3))

## Version 0.1.0 (2013-04-04):
- Initial release
