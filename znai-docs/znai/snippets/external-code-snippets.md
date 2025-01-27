# Embedding Content

To reduce documentation maintenance burden avoid copy and paste of code snippets.
Embed content by referencing existing files using the `:include-file:` plugin instead.  

    :include-file: file-name.js
    
This `include-` syntax will appear throughout the documentation and represents a family of custom Markdown extensions. 

:include-file: file-name.js

The file will be looked up using following rules:
* directory with a markup file
* root directory of a documentation
* all lookup paths listed in a `lookup-paths` file

# Syntax highlighting

Syntax highlighting is automatically selected based file extension. 
E.g. extensions `.c`, `.h`, `.cpp`, `.hpp` are treated as C++.  

    :include-file: simple.c
    
:include-file: simple.c

    :include-file: Hello.sc
    
:include-file: Hello.sc

Use `lang` to force a different syntax highlighting

    :include-file: simple.c {lang: "java"}
    
:include-file: simple.c {lang: "java"}

Note: File extensions and `lang` are case-insensitive.

# Title

    :include-file: file-name.js {title: "ES6 class"} 

Use the `title` property to specify a title.

:include-file: file-name.js {title: "ES6 class"} 

Use the `autoTitle` property to set `title` to be the file name.

    :include-file: file-name.js {autoTitle: true} 

:include-file: file-name.js {autoTitle: true} 


# Wide Code

Use the `wide` option to stretch wide code to occupy as much horizontal screen real estate as possible.  

    :include-file: WideCode.java {wide: true}
    
:include-file: WideCode.java {wide: true}

Without the `wide` option code will be aligned with the rest of the text and users can use scrollbars.   

:include-file: WideCode.java

Note: Good placement of a *Wide Code* element is at the end of a page or a section to show the full version of a code sample.

# Read More

If you have a file with large code snippet and you want to initially display only a small fraction use `readMore` 
option with an **optional** `readMoreVisibleLines` option to specify a number of initial lines displayed (default is 8).

    :include-file: LongFile.java {readMore: true, readMoreVisibleLines: 3}
    
:include-file: LongFile.java {readMore: true, readMoreVisibleLines: 3} 

# Highlights

Use the `highlight` option to bring readers attention to the important lines.

    :include-file: file-name.js {highlight: "export"}

:include-file: file-name.js {highlight: "export"}

It is recommended to pass a substring, but you can pass a line idx (starts from 0). 
Additionally you can combine two approaches and pass a list of things to highlight. 

    :include-file: file-name.js {highlight: ["export", 1]}

:include-file: file-name.js {highlight: ["export", 1]}

Note: Order of lines to highlight is reflected during presentation mode

Use the `highlightPath` option to highlight lines specified in a separate file. 

    :include-file: file-name.js {highlightPath: "lines-to-highlight.txt"}
    
:include-file: file-name.js {highlightPath: "lines-to-highlight.txt"}

:include-file: lines-to-highlight.txt {title: "lines-to-highlight.txt"}

# Extract Snippets

Use `startLine`, `endLine` to extract specific content by using marker lines.

:include-file: python-examples.py {title: "file with examples"} 

    :include-file: python-examples.py {startLine: "example: book trade", endLine: "example-end"}
     
:include-file: python-examples.py {title: "extracted example", startLine: "example: book trade", endLine: "example-end"}

Note: Lines match doesn't have to be exact, `contains` is used.
 
By default `startLine` and `endLine` are included in the rendered result. Use `excludeStartEnd: true` to remove markers.   

    :include-file: python-examples.py { 
        startLine: "example: book trade",
        endLine: "example-end",
        excludeStartEnd: true }

:include-file: python-examples.py {
    title: "extracted example", 
    startLine: "example: book trade",
    endLine: "example-end",
    excludeStartEnd: true }

Use `surroundedBy` and the same marker for start/end to streamline the snippets extract process
    
    :include-file: python-examples.py {
        title: "extracted example with surroundedBy",
        surroundedBy: "# example-cancel-trade"}

:include-file: python-examples.py {
  title: "extracted example with surroundedBy",
  surroundedBy: "# example-cancel-trade"}

Use `includeRegexp` to include only lines matching regexp(s).

    :include-file: python-examples.py {includeRegexp: "import"}
    :include-file: python-examples.py {includeRegexp: ["import"]}
    
:include-file: python-examples.py { includeRegexp: ["import"], title: "include by regexp" }

Use `excludeRegexp` to exclude lines matching regexp(s).

    :include-file: python-examples.py {excludeRegexp: "# example"}
    :include-file: python-examples.py {excludeRegexp: ["# example"]}
    
:include-file: python-examples.py { excludeRegexp: ["# example"], title: "exclude by regexp" }
