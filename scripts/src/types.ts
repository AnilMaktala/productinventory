export interface Issue {
  number: number;
  title: string;
  body: string | null;
  state: string;
  labels: Array<{ name: string }>;
  created_at: string;
  updated_at: string;
}

export interface LabelPrediction {
  label: string;
  confidence: number;
  reasoning: string;
}

export interface DuplicateMatch {
  issueNumber: number;
  title: string;
  similarity: number;
  reasoning: string;
}

export interface BedrockResponse {
  labels: LabelPrediction[];
  duplicates: DuplicateMatch[];
}
