import { ICandidate } from 'app/shared/model/candidate.model';

export interface IFile {
  id?: number;
  name?: string | null;
  dataContentType?: string | null;
  data?: string | null;
  candidate?: ICandidate | null;
}

export const defaultValue: Readonly<IFile> = {};
