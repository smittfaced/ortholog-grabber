# ortholog-grabber
Ortholog Grabber JAVA Runnable for use with GenePalette Software or Independently

 - Ortholog Grabber started as a project add-in for GenePalette. During development I split the code to allow for an independent version of OG to be developed alongside the original plugin. OG uses the UCSC Genome Browser to BLAT search a sequence and return the possible ortholog species matches of that sequence. The user then selects the sequences that meet their quality criteria and OG outputs a .fasta file containing all of the selected sequences. [genome.ucsc.edu/]
	
 - GenePalette is a powerful cross-platform and cross-species desktop application for genome sequence visualization and navigation. Users can download segments of genome sequence from NCBI’s GenBank database from a variety of organisms and annotate the sequences within a colorful graphical representation of the gene organization and intron/exon structure. [genepalette.org]


Welcome to OG!

	Using OG: Ortholog Grabber
		1. Input sequence -> Click 'Submit;
			(Sequence taken directly from GenePalette is un-editable!)
		2. Choose desired specie(s) to search for orthologous sequences by selecting buttons left of species name -> 'Submit'
			(If you prefer a specific published genome to search in, select it from the Genome dropdown menu)
		3. Choose desired specie(s) to get orthologous DNA sequence again using buttons to the left
		4. Select from the dropdown menu of 'hits' a sequence with desireable SPAN and original sequence IDENTITY percentage
			**If at any point you are dissatisfied with the results of a search, return to the previous page using 'back' and resubmit the search after making desired changes.
				
	Settings:
		- Managing Profiles
			OG allows users to easily switch between groups of species for Sequence Retrieval by assigning profiles to them.
			Creating a new profile is as easy as typing a name for the profile in the textfield and clicking 'Add Profile'.
			Once a profile has been added it will be appended to the list of profiles in the top left of the settings page.
			Clicking on a profile tab will allow you to select/deselect species you desire to be included in ortholog searches.
				(Having fewer species in a profile will allow for faster searches!)
			To delete a profile, select the profile tab and click 'Delete Profile'.
			Any changes to profiles will not be saved unless the user selects the 'Save Changes' button in the bottom right!!

		- Other Settings
			On sequence retrieval, DNA can be padded in the 3' and 5' directions by indicating the number of base pairs desired.
			Again, any changes to settings here will not be saved unless the 'Save Changes' button is clicked!

		- Helpful Tips
			Whichever profile is selected in the Settings menu will be used to conduct all of the species searches in OG.
			OG: Ortholog Grabber will remember the last selected profile on future instances of the application.
			After making changes to the Settings, remember to save them!
