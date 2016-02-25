Standalone Review Plugin for Intellij IDEA
===

This is a work in progress / prototype.  I would not recommend using this in its current incarnation.

The goal of this plugin is to enable a code reviewer to add code annotations (via the Gutter) to the current open project in Intellij IDEA.  Useful for the scenario where you are reviewing someone else's feature branch/hotfix and don't have the infrastructure setup for a centralized code review system (or just don't like Github's code review tooling).

All annotations are recorded in the workspace and can be exported to a flat text file for distribution via email or some other means.

Future goals:

* Show line level annotations in the Gutter
* Manage multiple reviews
* Cleaner input dialog to add annotations
  * Specify priority of comment: high, medium, low
  * Support for markdown (would enable snazy things like including code snippets in a comment)
* Support for customizable template for published review
* Editor view showing all annotations in the current Review
