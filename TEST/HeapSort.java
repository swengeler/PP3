/*
* @(#)HeapSortAlgorithm.java   1.0 95/06/23 Jason Harrison
*
* Copyright (c) 1995 University of British Columbia
*
* Permission to use, copy, modify, and distribute this software
* and its documentation for NON-COMMERCIAL purposes and without
* fee is hereby granted provided that this copyright notice
* appears in all copies. Please refer to the file "copyright.html"
* for further important copyright and licensing information.
*
* UBC MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
* THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
* TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
* PARTICULAR PURPOSE, OR NON-INFRINGEMENT. UBC SHALL NOT BE LIABLE FOR
* ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
* DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
*/

/**
* A heap sort demonstration algorithm
* SortAlgorithm.java, Thu Oct 27 10:32:35 1994
*
* Modified by Steven de Jong for Genetic Algorithms.
*
* Modified by Jo Stevens for practical session.
*
* Modified by Simon Wengeler for use in Project 1.3 of the Bachelor of
* Knowledge Engineering at Maastricht University (2016)
*
* @author Jason Harrison@cs.ubc.ca
* @version     1.0, 23 Jun 1995
*
* @author Steven de Jong
* @version     1.1, 08 Oct 2004
*
* @author Jo Stevens
* @version 1.2, 14 Nov 2008
*
* @author Simon Wengeler
* @version 1.3, 10 Jan 2016
*/
public class HeapSort {

	private static void downheap(CargoSpace cargo[], int k, int N)
    {
        CargoSpace cargoSpace = cargo[k - 1];

        while (k <= N/2)
        {
            int j = k + k;
            if ((j < N) && (cargo[j - 1].getTotalValue() > cargo[j].getTotalValue()))
            j++;

            if (cargoSpace.getTotalValue() <= cargo[j - 1].getTotalValue())
            break;

            else
            {
                cargo[k - 1] =cargo[j - 1];
                k = j;
            }
        }
        cargo[k - 1] = cargoSpace;
    }

	public static void sort(CargoSpace cargo[])
    {
        int N = cargo.length;

        for (int k = N/2; k > 0; k--)
        downheap(cargo, k, N);

        do
        {
            CargoSpace cargoSpace = cargo[0];
            cargo[0] = cargo[N - 1];
            cargo[N - 1] = cargoSpace;

            N = N - 1;
            downheap(cargo, 1, N);
        }
        while (N > 1);
    }

    public static void sortDownInd(Individual[] i) {
        int N = i.length;

        for (int k = N/2; k > 0; k--)
            downheap(i, k, N);

        do {
            Individual T = i[0];
            i[0] = i[N - 1];
            i[N - 1] = T;

            N = N - 1;
            downheap(i, 1, N);
        } while (N > 1);
    }

    public static void sortUpInt(int[] i) {
        int N = i.length;

        for (int k = N/2; k > 0; k--)
            upheap(i, k, N);

        do {
            int T = i[0];
            i[0] = i[N - 1];
            i[N - 1] = T;

            N = N - 1;
            upheap(i, 1, N);
        } while (N > 1);
    }

    private static void downheap(Individual[] i, int k, int N) {
        Individual T = i[k - 1];

        while (k <= N/2) {
            int j = k + k;
            if ((j < N) && (i[j - 1].getFitness() > i[j].getFitness()))
                j++;

            if (T.getFitness() <= i[j - 1].getFitness())
                break;

            else {
                i[k - 1] = i[j - 1];
                k = j;
            }
        }
        i[k - 1] = T;
    }

    private static void upheap(int[] i, int k, int N) {
        int T = i[k - 1];

        while (k <= N/2) {
            int j = k + k;
            if ((j < N) && (i[j - 1] < i[j]))
                j++;

            if (T >= i[j - 1])
                break;

            else {
                i[k - 1] = i[j - 1];
                k = j;
            }
        }
        i[k - 1] = T;
    }
}
