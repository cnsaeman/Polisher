#!/usr/bin/env ruby

# Introduce clean levels: 0: not modified, 1: \href{}{}, 2: equations removed

# Tips:
# Name in BibTeX file with hyphen: Hide hyphen as Schommer{--}Pries

# general rules, applied everywhere, to body of Text, formulas and bibliography

=begin Testin regexps:
s=" some string" 
r="some regexp"
s.to_enum(:scan, (Regexp.new t)).map { Regexp.last_match }
=end


# raw latex
level0_rules=[
    ]

# header removed, minor clean up
level1_rules=[
        ## common typos
        ["Doubled word", "\\b(\\w+)\\s+\\1\\b"],
        ["Double article", "The the "],
        ["Weird insertion of dot, missing space?","[A-Z][a-z]+\\.[A-Z][a-z]+"],
        ["[P]rincipal/le bundle","rinciple bundle"],
        ["article a to an?","(^|\s)[Aa] [aei][a-z]+"],
        ["article a to an?","(^|\s)[Aa] o[^n][a-z]+"],
        ["article a to an?","(^|\s)[Aa] on[^e][a-z]+"],
        ["article a to an?","(^|\s)[Aa] \\$L"],
        ["article a to an?","(^|\s)[Aa] \\$\\\\infty"],
        ["article an to a?","(^|\s)[Aa]n [^aeiou\($][a-z]+"],
        ["article a to an before curly N?","(^|\S)[Aa] \\$\\\\CN"],
        ["article a to an?","(?i)a \\$\\\\CN"],
        ["a M2-brane or a M5-brane instead of \"an ...\"","a M\\d"],
        ["teh instead of the","(^|\s)(?i)teh\\S"],
        ["spelling: losing","loosing"],
        ["spelling: worthwhile?","worth while"],
        ["spelling","self-Dual"],
        ["spelling","m-[Bb]rane"],

        ## problematic formulations
        ["name of reference (section, appendix, etc.) missing","(^|\s)[Ss]ee \\\\ref"],
        ["name of reference (section, appendix, etc.) missing","(^|\s)[Ss]ee~\\\\ref"],

        ## typesetting
        ["Space in front of \\eqref","[^,] \\\\eqref"],
        ["Space in front of \\cite","[^,] \\\\cite"],
        ["Space in front of \\ref","[^,] \\\\ref"],
        ["tilde between degree and number","of degree \\$[+\\-123456789]."],
        ["space before number should be ~","[a-z.]+ \\d+\\."],
        ["space before or after number should be ~","[a-z.]+ \\d+ [a-z.]+"],
        ["fullstop at end of equation incorrect?","\\.\\s+\\\\end{equation}\\s+[a-z]"],
        ["e.g. without backslash","e\\.g\\. "],
        ["i.e. without backslash","i\\.e\\. "],
        ["etc. without backslash","etc.\s[a-z]"],
        ["cf. without backslash","cf\\. "],
        ["no space before footnote","[ ]\\\\footnote"],
        ["footnote attached to formula","\\$\\\\footnote"],
        ["negative numbers: -1 -> $-1$","\\s-\\d+[ .;!?,~]"],

        # with Martin:
        #["abelian -> Abelian ","abelian"],
        # without Martin:
        ["\\ldots should be changed to \\dots","\\\\ldots"],
        ["\\cdots should be changed to \\dots","\\\\cdots"],
        ["Abelian -> abelian ","[^.?!]\\bAbelian"],

        ##
        ["Section heading starting with lower case", "\\\\(part|chapter|section|subsection|subsubsection|paragraph){[a-z]"],
        ["Section heading ending with punctuation symbol", "\\\\(part|chapter|section|subsection|subsubsection)\\{[^@]*?[\.,;:]\\s*\\}"],
    
        ## colloquialisms, etc.
        ["replace by many","(?i)a large number of"],
        ["replace by how","(?i)the way in which"],
        ["contraction or wrong possessive pronoun","[Ii]t's"],
        ["no contractions","(?i)[dw]on't"],
        ["no contractions","(?i)doesn't"],
        ["no contractions","(?i)didn't"],
        ["no contractions","(?i)isn't"],
        ["no contractions","(?i)we'll"],
        ["no contractions","(?i)we've"],
        ["no contractions","(?i)let's"],
        ["colloquialism","(?i)how come"],
        ["colloquialism","(?i)contraption"],
        ["colloquialism","(?i)kind of"],
        ["colloquialism","(?i)sort of"],
        ["don't use so-called, negative connotation","so-called"],
        ["bad word","necessitate"],
    
        ## grammar, general language:
        ["example of not for","xample for"],

        ## multiline_rules
        ["fullstop ending equation, but lowercase continuation","[ ~]\\.@@\\d+@@\\\\end{equation}@@\\d+@@[a-z]"],
        ["comma ending equation, but uppercase continuation","[ ~],@@\\d+@@\\\\end{equation}@@\\d+@@[A-Z]"],
        ["no fullstop ending equation, but uppercase continuation","[^.}]@@\\d+@@\\\\end{equation}@@\\d+@@[A-Z]"],
        ["no comma at end of equation before where","[^,}]@@\\d+@@\\\\end{equation}@@\\d+@@where"],
        ["fullstop ending equation, but lowercase continuation","[ ~]\\.@@\\d+@@\\\\end{aligned}@@\\d+@@\\\\end{equation}@@\\d+@@[a-z]"],
        ["comma ending equation, but uppercase continuation","[ ~],@@\\d+@@\\\\end{aligned}@@\\d+@@\\\\end{equation}@@\\d+@@[A-Z]"],
        ["no comma at end of equation before where","[^,}]\\s*@@\\d+@@\\\\end{aligned}@@\\d+@@\\\\end{equation}@@\\d+@@where"],
        ["no fullstop ending equation, but uppercase continuation","[^.}\\s]\\s+@@\\d+@@\\\\end{aligned}@@\\d+@@\\\\end{equation}@@\\d+@@[A-Z]"],
        ["fullstop ending equation, but lowercase continuation","[ ~]\\.@@\\d+@@\\\\end{gathered}@@\\d+@@\\\\end{equation}@@\\d+@@[a-z]"],
        ["comma ending equation, but uppercase continuation","[ ~],@@\\d+@@\\\\end{gathered}@@\\d+@@\\\\end{equation}@@\\d+@@[A-Z]"],
        ["no comma at end of equation before where","[^,}]\\s*@@\\d+@@\\\\end{gathered}@@\\d+@@\\\\end{equation}@@\\d+@@where"],
        ["no fullstop ending equation, but uppercase continuation","[^.}\\s]\\s+@@\\d+@@\\\\end{gathered}@@\\d+@@\\\\end{equation}@@\\d+@@[A-Z]"],
        ["paragraph ends without punctuation symbol (two lines up)","@@\\d+@@[^\\\\][^@]+[a-zA-Z]@@\\d+@@@@\\d+@@"],
        ["\"indeed\" repeated in close proximity","indeed.{2,300}indeed"],
        ["\"readily\" repeated in close proximity","readily.{2,300}readily"]
    ]

# full equations removed
level2_rules=[
        ["no space before/after inline formula","\\w\\$[^-,~\\s.;:}\\)'\\\\\\]@]+",['N\$Q\$-',"\\$\\w+?\\$th\\s"]],    
        ["no space before/after inline formula","[^-\\s@~{}\\('\\[]\\$\\w",['N\$Q\$-',"\\$\\w+?\\$th\\s"]],    
    ]

# only equations
level3_rules=[
        ["in equation, Space necessary before ,","[^~)],$"],
        ["in equation, space necessary before .","[^~)]\\.$"]
    ]

# equations removed, inline equations to $inline$
level4_rules=[
        ["no space before ."," \\."],
        ["no space before ,","[ ],"],
        ["dash should be --- ","[ ]-[ ]"],
    ]

# rules with individual adjustments, regexps in last term are first deleted, applied to level 1
level5_rules=[
        ["Double names requires -- as e.g. in Yang--Mills etc.","[^{~][A-Z][a-z]+-[A-Z][a-z]+",['Pseudo-']],
    ]

# bbl rules, only applied to bibliography
bbl_rules=[
    ["math arXiv without subject classifier","math\/"],
    ["Phys without fullstop","Phys[ ]"],
    ["Abelian -> abelian ","[^.?!]\\bAbelian"],
    ["JHEPxx without space","JHEP\\d\\d "],
    ["JHEPxx without space","JHEP\\d\\d\\d\\d "],
    ["2-number JHEP identifier", "JHEP \\{\\\\bf \\d\\d\\}"]
]

def apply_rules(rules,text,issues)
  rules.each do |r|
    adjusted_text=text
    if r.length>2
        r[2].each do |to_delete|
            adjusted_text.gsub!((Regexp.new to_delete),'')
        end
    end
    matches=adjusted_text.to_enum(:scan, (Regexp.new r[1])).map { Regexp.last_match }
    if matches
        matches.each do |match|
            (1..match.length).each do |i|
                e1=adjusted_text.rindex('@@',match.begin(i-1))
                if e1
                    e2=(e1 ? adjusted_text.rindex('@@',e1-1) : 0)
                    e2=e1 unless e2
                else
                    e1=4
                    e2=0
                end
                f1=adjusted_text.index('@@',e1+2)
                f1=e1+2 unless f1
                issues << "Line #{adjusted_text[e2+2..e1-1]}: #{r[0]} : #{match}\n >>#{adjusted_text[e1+2..f1-1]}\n---\n"
            end
        end
    end
  end
end    

puts ""
puts "Polisher v0.2 with Rules v0.2"
puts "-----------------------------"
puts "(w) C Saemann"
puts ""

name=ARGV[0]
issues=[]

issues << "Issues in TeX:"
issues << "----------------------"
print "Reading file: '#{name}.tex' ... "
begin
  infile=File.new(name+'.tex','r')
  contents=infile.read.force_encoding("ISO-8859-1").encode("utf-8", replace: nil)
  contents.gsub!(/\r\n?/, "\n")
  # prepare raw
  level0=''
  i=0
  contents.each_line do |line|
      i=i+1       
      level0=level0+"@@#{i}@@"+line[0..-2]
  end    
  infile.close
  puts "complete."

  # prepare without header
  start=level0.index('\begin{document}')
  level1=level0[start..-1]
  # remove standard latex stuff, remove more than one empty line
  level1.gsub!(/\acknowledgements/,'')
  level1.gsub!(/\datamanagement/,'')
  level1.gsub!(/@\s+@/,'@@')
  level1.gsub!(/@@@\d+@@@(?=@\d+@@)/,'@@')
  level1.gsub!(/@@@\d+@@@(?=@\d+@@)/,'@@')
  
  # prepare without equations
  level2=level1[start..-1]
  # prepare only equations
  level3=""
  while level2.index('\begin{equation}')
      i=level2.index('\begin{equation}')
      j=level2.index('\end{equation}',i)
      level3=level3+"\n"+level2[i+16..j-1]
      level2=level2[0..i-1]+level2[j+14..-1]
  end
  while level2.index('\begin{equation*}')
      i=level2.index('\begin{equation*}')
      j=level2.index('\end{equation*}',i)
      level3=level3+"\n"+level2[i+16..j-1]
      level2=level2[0..i-1]+level2[j+14..-1]
  end
  while level2.index('$$')
      i=level2.index('$$')
      j=level2.index('$$',i)
      level3=level3+"\n"+level2[i+16..j-1]
      level2=level2[0..i-1]+level2[j+14..-1]
  end
  # prepare without inline formulas
  level4=level2.gsub(/\$[^$]+\$/, "$inline$")

#   File.open('level0', 'w') { |file| file.write(level0) }
#   File.open('level1', 'w') { |file| file.write(level1) }
#   File.open('level2', 'w') { |file| file.write(level2) }
#   File.open('level3', 'w') { |file| file.write(level3) }
#   File.open('level4', 'w') { |file| file.write(level4) }
  
  puts "level0_rules"
  apply_rules(level0_rules,level0,issues)
  puts "level1_rules"
  apply_rules(level1_rules,level1,issues)
  puts "level2_rules"
  apply_rules(level2_rules,level2,issues)
  puts "level4_rules"
  apply_rules(level3_rules,level3,issues)
  puts "level3_rules"
  apply_rules(level4_rules,level4,issues)
  apply_rules(level5_rules,level1,issues)
  
  puts "done."
rescue
  puts "!!Error"
  issues << "!!Error reading TeX-file: #{name}.tex"
end  

issues << "Issues in BBL:"
issues << "----------------------"
print "Reading file: '#{name}.bbl' ... "
begin
  infile=File.new(name+'.bbl','r')
  contents=infile.read.force_encoding("ISO-8859-1").encode("utf-8", replace: nil)
  contents.gsub!(/\r\n?/, "\n")
  # prepare raw
  level0=''
  i=0
  contents.each_line do |line|
      i=i+1       
      level0=level0+"@@#{i}@@"+line[0..-2]
  end    
  infile.close
  puts "complete."
  
  apply_rules(level5_rules,level0,issues)
  apply_rules(level1_rules,level0,issues)
  apply_rules(bbl_rules,level0,issues)
rescue
  puts "!!Error"
  issues << "!!Error reading BBL-file: #{name}.bbl"
end  

puts ""
issues.each do |i|
  puts i
end


