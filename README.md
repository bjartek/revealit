# RevealIT

Combine html/md files into a reveal.js presentation. 


- slides are sorted using natural ordering. Suggested naming is numbers. 1.md, 2.md aso
- creates a `<section>` for each html/md file in the _files_ parameter
- name your slides 1.1.html 1.2.html to make a vertical (wraps all 1.* sections in another sections)
- replaces code fragments referencing files with the content of the files
  -  `<code>Foo.scala</code>` with `<code>(the content of Foo.scala)</code>`
  -  `<code>Foo.java:5-10</code>` with `<code>(the lines from 5-10 of Foo.java)</code>`
- the _template_ is used in the following way
  - _#content#_ is replaced with all the sections
  - _#transition#_ is replaced with the chosen transition
  - _#name#_ is replaced with the name of the presentation 
 - Assembled with the scala-library for convenience. See buid.sbt if you do not want this for a slimmer jar.

## How to use

 - download [reveal.js](https://github.com/hakimel/reveal.js/downloads) and unzip it
 - copy over the contents from the `revealit` folder into the unziped folder
 - optionally compare `index.html` and `template.html` into a new `template.html 
 - `java -jar revealit.jar` will build all html/md files in slides into `index.html` overwriting it.

## Usage
<pre>Usage: java -jar revealit.jar  [options] 

  -s <value> | --slides <value>
        Where to find the files with slides default: slides
  -rf <value> | --resultFile <value>
        Name of result file default: index.html
  -n <value> | --name <value>
        Title of HTML page default: Reveal.js Presentation
  -t <value> | --title <value>
        Transition. One of default|cube|concave|page|linear|zoom|none
default: default
  -tpl <value> | --template <value>
        Template to use  default: template.html
</pre>
## Depencies
 - [sbt-assembly](https://github.com/sbt/sbt-assembly) to assemble the jar
 - [scopt](https://github.com/scopt/scopt) to parse CLI args
 - [specs2](http://etorreborre.github.com/specs2/) for testing

## License

Copyright © 2012 Bjarte Stien Karlsen

Distributed under the
[Mozilla Public License](http://mozilla.org/MPL/2.0/), version 2.0.

