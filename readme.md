To run polisher, you only need Polisher.jar and rules.cfg in the same directory.

To start polisher, execute java -jar Polisher.jar in the installation directory.

If you open a .tex-file, you should have the corresponding .bbl-file in the same folder.

## Introduction

This is a little java program that highlights problematic sections of drafts of scientific papers written in LaTeX. It allows you to edit these things directly. Essentially all of the highlighting can be configured via regular expressions, which are stored in "rules.cfg".

The software is currently in an beta state. Suggestions for improvement are welcome.

## Remarks

Currently, polisher can only save changes to the body of the paper and not to the bibliography.
