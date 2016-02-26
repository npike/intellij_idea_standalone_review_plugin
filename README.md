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



Helpful links
http://bjorn.tipling.com/how-to-make-an-intellij-idea-plugin-in-30-minutes
http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/creating_an_action.html
https://github.com/syllant/idea-plugin-revu/blob/a07f78dd3b6e74370c8ad73cebbde80df8f9fced/src/main/java/org/sylfra/idea/plugins/revu/ui/forms/issue/IssueMainForm.java
https://github.com/consulo-trash/consulo-bitbucket/blob/17ac8ee0694afb7026e9fd8e5ef5fdeaa979cad8/src/org/bitbucket/connectors/jetbrains/ui/HtmlMessageDialog.java
https://github.com/consulo-trash/consulo-bitbucket/blob/17ac8ee0694afb7026e9fd8e5ef5fdeaa979cad8/src/org/bitbucket/connectors/jetbrains/ui/HtmlPanel.form
https://raw.githubusercontent.com/consulo-trash/consulo-bitbucket/17ac8ee0694afb7026e9fd8e5ef5fdeaa979cad8/src/org/bitbucket/connectors/jetbrains/ui/HtmlPanel.java
https://www.jetbrains.com/idea/help/exporting-and-importing-settings.html
http://wicketforge.googlecode.com/svn/trunk/src/wicketforge/action/ui/ExtractHtmlTextDialog.java