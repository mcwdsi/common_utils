package edu.ufl.bmi.util;

import java.util.HashMap;

public class LabelSequenceMap {
	HashMap<String, Integer> _labelToSequence;
	HashMap<Integer, String> _sequenceToLabel;
	
	public LabelSequenceMap() {
		_labelToSequence = new HashMap<String, Integer>();
		_sequenceToLabel = new HashMap<Integer, String>();
	}
	
	public void addLabelAndSequencePair(String label, int seq) {
		Integer seqInt = new Integer(seq);
		if (!_labelToSequence.containsKey(label)) _labelToSequence.put(label, seqInt);
		else throw new IllegalArgumentException("Labels must be unique.  Sequence number has already been assigned to " + label);
		
		if (!_sequenceToLabel.containsKey(seqInt)) _sequenceToLabel.put(seqInt, label);
		else throw new IllegalArgumentException("Sequence numbers must be unique.  Label has already been assigned to " + seqInt);
	}
	
	public String getLabelForSeq(int seq) {
		Integer seqInt = new Integer(seq);
		return _sequenceToLabel.get(seqInt);
	}
	
	public int getSeqForLabel(String label) {
		Integer seq = _labelToSequence.get(label);
		if (seq == null) return Integer.MIN_VALUE;
		else return seq;
	}
	
}
